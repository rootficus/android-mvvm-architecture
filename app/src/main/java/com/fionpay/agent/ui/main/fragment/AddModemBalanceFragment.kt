package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentAddModemBalanceBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.AddModemBalanceFragmentModule
import com.fionpay.agent.ui.main.di.DaggerAddModemBalanceFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import javax.inject.Inject

class AddModemBalanceFragment :
    BaseFragment<FragmentAddModemBalanceBinding>(R.layout.fragment_add_modem_balance) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private lateinit var dashBoardListAdapter: DashBoardListAdapter
    private var arrayList: ArrayList<TransactionModel> = arrayListOf()
    private var modemId = ""
    private var modemItemModel = ModemItemModel()
    var totalBalance: Double? = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {
        getBundleData()
        mDataBinding.topHeader.txtHeader.text = getString(R.string.add_modems)
        val gson = Gson()
        val json: String? = viewModel.getDashBoardDataModel()
        val obj: DashBoardItemResponse =
            gson.fromJson(json, DashBoardItemResponse::class.java)
        mDataBinding.txtExistingBalance.text = "৳${obj.currentBalance.toString()}"
        mDataBinding.labelTotalBalance.text = "New balance will be: ৳${obj.currentBalance}"
        mDataBinding.etAddBalance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lastName = s.toString()

                if (lastName.isNotEmpty()) {
                    mDataBinding.lastBalanceVerified.visibility = View.VISIBLE
                } else {
                    mDataBinding.lastBalanceVerified.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    val newVal = s.toString().toDouble()
                    totalBalance = obj.currentBalance?.minus(newVal)
                    if (totalBalance != null && totalBalance!! > 0.0) {
                        mDataBinding.labelTotalBalance.text = "New balance will be: ৳$totalBalance"
                    } else {
                        "New balance will be: ৳${obj.currentBalance}"
                    }

                } else {
                    mDataBinding.labelTotalBalance.text =
                        "New balance will be: ৳${obj.currentBalance}"
                }

            }
        })

        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }

        mDataBinding.btnNext.setOnClickListener {
            if (mDataBinding.etAddBalance.text.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_BALANCE)
                )
            } else {
                if (totalBalance != null && totalBalance!! > 0.0) {
                    val bundle = Bundle().apply {
                        putString("modemBalance", mDataBinding.etAddBalance.text.toString())
                        putSerializable("modemItemModel", modemItemModel)
                    }
                    Navigation.findNavController(requireView())
                        .navigate(
                            R.id.action_navigation_addModemBalanceFragment_to_navigation_confirmModemFragment,
                            bundle
                        )
                    //addModemBalance()
                } else {
                    Snackbar.make(requireView(), "Not a valid amount", Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            //modemId = bundle.getString("modem_id").toString()
            modemItemModel = bundle.getSerializable("modemItemModel") as ModemItemModel
        }
    }

    private fun initializeDagger() {
        DaggerAddModemBalanceFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .addModemBalanceFragmentModule(AddModemBalanceFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }


}