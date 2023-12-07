package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.RefundRequest
import com.fionpay.agent.data.model.response.B2BResponse
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.databinding.FragmentB2bBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.B2BListAdapter
import com.fionpay.agent.ui.main.di.B2BFragmentModule
import com.fionpay.agent.ui.main.di.DaggerB2BFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import javax.inject.Inject


class B2BFragment :
    BaseFragment<FragmentB2bBinding>(R.layout.fragment_b2b) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private lateinit var b2BListAdapter: B2BListAdapter
    private var arrayList: ArrayList<B2BResponse> = arrayListOf()
    lateinit var b2bDetailScreenFragment: B2bDetailScreenFragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()

    }

    private fun initializeDagger() {
        DaggerB2BFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .b2BFragmentModule(B2BFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    override fun onResume() {
        super.onResume()
        setSpinnerData()
    }

    private fun initialization() {
        setSpinnerData()
        b2bDetailScreenFragment = B2bDetailScreenFragment()
        val gson = Gson()
        val json: String? = viewModel.getDashBoardDataModel()
        val obj: DashBoardItemResponse =
            gson.fromJson(json, DashBoardItemResponse::class.java)
        mDataBinding.refreshButton.setOnClickListener {

            b2bDetailScreenFragment.isCancelable = false
            b2bDetailScreenFragment.listener = modemDetailScreenActionListener
            val bundle = Bundle()
            //bundle.putSerializable(GetModemsByFilterResponse::class.java.name, GetModemsByFilterResponse)
            bundle.putString("CurrentBalance", "${viewModel.getCurrentAgentBalance().toString()}")
            b2bDetailScreenFragment.arguments = bundle
            activity?.supportFragmentManager?.let {
                b2bDetailScreenFragment.show(
                    it,
                    "ActionBottomDialogFragment"
                )
            }
        }

        setAdapter()
        getRefundRecord()

    }

    private fun setSpinnerData() {
        val modemList = viewModel.getModemsListDao()
        val bankList = viewModel.getBanksListDao()
        val phoneNumbers = arrayListOf<String>()
        val bankNames = arrayListOf<String>()
        val transactionType = arrayListOf("All Types", "Cash In", "Cash Out")

        //Set Modem Adapter
        phoneNumbers.add(0, "All Modems")
        modemList?.forEach {
            phoneNumbers.add(it.phoneNumber.toString())
        }
        val modemAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            phoneNumbers
        )
        modemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Set Bank Adapter
        bankNames.add(0, "All Banks")
        bankList?.forEach {
            bankNames.add(it.bankName.toString())
        }


        //Set Transaction Type Adapter

        val transactionTypeAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            transactionType
        )
        transactionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    }

    private fun setAdapter() {
        b2BListAdapter = B2BListAdapter(arrayList)
        mDataBinding.recentModemsList.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentModemsList.adapter = b2BListAdapter
    }

    private val modemDetailScreenActionListener =
        object : B2bDetailScreenFragment.BottomDialogEvent {

            override fun onAddRequest(
                amount: Double
            ) {
                refundBalance(amount)
            }

        }

    private fun getRefundRecord() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getUserId()
                ?.let { viewModel.getB2BRecord(viewModel.getUserId().toString()) }
            viewModel.getB2BRecordResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
                        b2BListAdapter.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            Snackbar.make(
                requireView(),
                getString(R.string.NO_INTERNET_CONNECTION),
                Snackbar.LENGTH_LONG
            ).show()
        }

    }


    private fun refundBalance(amount: Double) {
        if (networkHelper.isNetworkConnected()) {
            viewModel.returnModemBalance(RefundRequest(viewModel.getUserId(), amount))
            viewModel.returnModemBalanceResponseResponseModem.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        if (b2bDetailScreenFragment.isVisible) {
                            b2bDetailScreenFragment.dismiss()
                            viewModel.setCurrentAgentBalance(it.data?.balance.toString())
                            getRefundRecord()
                        }
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        showErrorMessage(it.message)
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showMessage(
                mActivity.getString(R.string.NO_INTERNET_CONNECTION)
            )
        }
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<B2BResponse> = ArrayList()

        for (item in arrayList) {
            // checking if the entered string matched with any item of our recycler view.

            if (text.isDigitsOnly()) {
                if (item.modem?.contains(text) == true) {
                    // if the item is matched we are
                    filteredList.add(item)
                }
            } else if (text.isEmpty()) {
                filteredList.clear()
                filteredList.addAll(arrayList)
            } else {
                if (item.transactionId?.contains(text) == true) {
                    // if the item is matched we are
                    filteredList.add(item)
                }
            }
        }
        if (text.isEmpty()) {
            filteredList.clear()
            filteredList.addAll(arrayList)
        }

        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            showMessage(getString(R.string.no_data_found))
        } else {
            // at last we are passing that filtered
            b2BListAdapter?.filterList(filteredList)
        }
    }
}