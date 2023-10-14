package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.GetPendingModemRequest
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.databinding.AlterPendingDialogBinding
import com.fionpay.agent.databinding.FragmentPendingBinding
import com.fionpay.agent.databinding.ItemTransactionManagerBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.BleManagerListAdapter
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.adapter.PendingListAdapter
import com.fionpay.agent.ui.main.di.DaggerPendingFragmentComponent
import com.fionpay.agent.ui.main.di.PendingFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class PendingFragment : BaseFragment<FragmentPendingBinding>(R.layout.fragment_pending) {

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
    private lateinit var pendingListAdapter: PendingListAdapter
    private var arrayList: ArrayList<PendingModemResponse> = arrayListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()

    }

    private fun initializeDagger() {
        DaggerPendingFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .pendingFragmentModule(PendingFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initialization() {

        mDataBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0) {
                    filter("")
                }

            }
        })

        mDataBinding.searchButton.setOnClickListener {
            filter(mDataBinding.searchView.text.toString())
        }
        mDataBinding.refreshButton.setOnClickListener {
            getPendingModemRecord()
        }
        setAdapter()
        getPendingModemRecord()
    }

    private fun setAdapter() {
        pendingListAdapter = PendingListAdapter(arrayList)
        pendingListAdapter?.listener = cardListener
        mDataBinding.pendingModemsList.layoutManager = LinearLayoutManager(context)
        mDataBinding.pendingModemsList.adapter = pendingListAdapter
    }

    private val cardListener = object : PendingListAdapter.CardEvent {
        override fun onCardClicked(pendingModemResponse: PendingModemResponse) {
            showAlertDialog(pendingModemResponse)
        }
    }

    private fun getPendingModemRecord() {
        if (networkHelper.isNetworkConnected()) {
            viewModel.getPendingRequest(GetPendingModemRequest(10, 1))
            viewModel.getPendingModemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        arrayList.clear()
                        progressBar.dismiss()
                        it.data?.let { it1 -> arrayList.addAll(it1) }
                        pendingListAdapter.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        if (it.message == "Invalid access token") {
                            sessionExpired()
                        } else {
                            showMessage(it.message.toString())
                        }
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            Snackbar.make(requireView(), "No Internet", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun showAlertDialog(item: PendingModemResponse) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val binding =  AlterPendingDialogBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)
        val alertDialog: AlertDialog = dialogBuilder.create()
        if (!item.bankImage.isNullOrEmpty()) {
            Glide.with(requireContext())
                .asBitmap()
                .centerInside()
                .load(item.bankImage)
                .error(R.drawable.bank_icon)
                .into(binding.imageBank)
        }
        setPaymentStatusView(item, binding)
        binding.txtSuccess.visibility=  View.VISIBLE
        binding.txtSuccess.text = "Pending"
        binding.txtSuccess.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.pending
            )
        )
        binding.txtSuccess.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.activePendingBg))
        binding.txtAmount.text = "৳${item.amount}"
        if (item.paymentType == "Cash In") {
            binding.txtTransactionId.text = "${item.customer.toString()}"
        } else {
            binding.txtTransactionId.text = "#${item.transactionId.toString()}"
        }
        binding.txtDate.text = Utility.convertTransactionDate(item.date)
        binding.txtBankType.text = item.bankType

        alertDialog.show()
    }

    private fun setPaymentStatusView(
        item: PendingModemResponse,
        binding: AlterPendingDialogBinding
    ) {
        when (item.paymentType) {
            "Cash In" -> {
                binding.labelCustomerNumber.text = item.paymentType.toString()
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.reject
                    )
                )
            }

            "Cash Out" -> {
                binding.labelCustomerNumber.text = item.paymentType.toString()
                binding.txtAmount.setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.greenColor
                    )
                )
            }
        }
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<PendingModemResponse> = ArrayList()
        for (item in arrayList) {
            // checking if the entered string matched with any item of our recycler view.

            if (text.isDigitsOnly()) {
                if (item.customer?.contains(text) == true) {
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
            Snackbar.make(requireView(), "No Data Found..", Snackbar.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            pendingListAdapter?.filterList(filteredList)
        }
    }

}