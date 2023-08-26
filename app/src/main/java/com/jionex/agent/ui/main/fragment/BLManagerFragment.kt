package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jionex.agent.R
import com.jionex.agent.data.model.request.GetBalanceByFilterRequest
import com.jionex.agent.data.model.response.GetBalanceManageRecord
import com.jionex.agent.databinding.CustomBleDialogBinding
import com.jionex.agent.databinding.FragmentBlManagerBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.adapter.BleManagerListAdapter
import com.jionex.agent.ui.main.di.BLManagerFragmentModule
import com.jionex.agent.ui.main.di.DaggerBLManagerFragmentComponent
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.Constant
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
import com.jionex.agent.utils.Utility
import javax.inject.Inject


class BLManagerFragment : BaseFragment<FragmentBlManagerBinding>(R.layout.fragment_bl_manager) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }

    private var bleManagerListAdapter: BleManagerListAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView()
    }

    private fun initializeDagger() {
        DaggerBLManagerFragmentComponent.builder().appComponent(JionexSDK.appComponent)
            .bLManagerFragmentModule(BLManagerFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        setBleAdapter(-1)
        //getStatusCount()
        mDataBinding.chipGroup.setOnCheckedChangeListener { chipGroup, i ->
            val chip = chipGroup.findViewById<Chip>(i)
            if (chip != null) {
                when (chip.id) {
                    mDataBinding.chipAllTransactions.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.All.action)
                    }

                    mDataBinding.chipSuccess.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.SUCCESS.action)
                    }

                    mDataBinding.chipPending.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.PENDING.action)
                    }

                    mDataBinding.chipRejected.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.REJECTED.action)
                    }

                    mDataBinding.chipRejected.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.APPROVED.action)
                    }

                    mDataBinding.chipDanger.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.DANGER.action)
                    }
                }
            }
        }
    }

    private fun setBleAdapter(filter:Int) {
        var arrayList = viewModel.getBalanceManageRecord(filter)
        Log.d("getBalanceByFilterApi", "::${filter},${arrayList.size}")
        bleManagerListAdapter = BleManagerListAdapter(arrayList)
        bleManagerListAdapter?.listener = cardListener
        mDataBinding.recentTrendingView.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentTrendingView.adapter = bleManagerListAdapter
        bleManagerListAdapter?.notifyDataSetChanged()
    }

    private val cardListener = object : BleManagerListAdapter.CardEvent {
        override fun onCardClicked(getBalanceManageRecord: GetBalanceManageRecord) {
            showCustomDialog(getBalanceManageRecord)
        }
    }

    private fun showCustomDialog(getBalanceManageRecord: GetBalanceManageRecord) {
        val builder = AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog).create()
        val binding = CustomBleDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        binding?.textAAccount?.text = getBalanceManageRecord.agentAccountNo.toString()
        binding?.textAmount?.text = getBalanceManageRecord.amount.toString()
        binding?.textCAccount?.text = getBalanceManageRecord.customerAccountNo.toString()
        binding?.textCommission?.text = getBalanceManageRecord.commision.toString()
        binding?.textDate?.text =
            Utility.convertUtc2Local(getBalanceManageRecord.date.toString())
        binding?.textSender?.text = getBalanceManageRecord.sender.toString()
        binding?.textLastBalance?.text = getBalanceManageRecord.lastBalance.toString()
        binding?.textOldBalance?.text = getBalanceManageRecord.oldBalance.toString()
        binding?.textType?.text = getBalanceManageRecord.bType.toString()
        binding?.textStatus?.text = getBalanceManageRecord.status.toString()
        binding?.textLastTransactionId?.text = getBalanceManageRecord.transactionId.toString()
        if (getBalanceManageRecord.status.equals("pending", true)) {
            binding.btnReject.visibility = View.GONE
            binding.btnAccept.visibility = View.GONE
        } else {
            binding.btnReject.visibility = View.VISIBLE
            binding.btnAccept.visibility = View.VISIBLE
        }
        binding.btnReject.setOnClickListener {
            builder.dismiss()
        }
        binding.imageClose.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun getBalanceByFilterApi(filter: Int) {
        if (networkHelper.isNetworkConnected()) {
            val getBalanceByFilterRequest = GetBalanceByFilterRequest(
                page_size = 50,
                page_number = 1,
                balance_manager_filter = filter

            )
            viewModel.getBalanceByFilter(
                getBalanceByFilterRequest
            )
            viewModel.getBalanceManageRecordModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        setBleAdapter(filter)
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        }
    }

    private fun getStatusCount() {

        if (networkHelper.isNetworkConnected()) {
            viewModel.getStatusCount()
            viewModel.getStatusCountResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        val getStatusCountResponse = it.data
                        viewModel.setBLSuccess(getStatusCountResponse?.success)
                        viewModel.setBLApproved(getStatusCountResponse?.approved)
                        viewModel.setBLDanger(getStatusCountResponse?.danger)
                        viewModel.setBLSuccess(getStatusCountResponse?.success)
                        viewModel.setBLPending(getStatusCountResponse?.pending)
                        viewModel.setBLRejected(getStatusCountResponse?.rejected)
                        setBleFilterCount()
                    }

                    Status.ERROR -> {
                        Log.i("Status Count", "Error")
                        //setDrawerData()
                    }

                    Status.LOADING -> {
                    }
                }
            }
        } else {
            showMessage(getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private fun setBleFilterCount() {
        var totalCount = viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.SUCCESS.action) +
                viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.PENDING.action) +
                viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.APPROVED.action) +
                viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.DANGER.action)+
                viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.REJECTED.action)
        mDataBinding.chipAllTransactions.text =
            getString(R.string.bl_all_transactions) + " (" + totalCount + ")"
        mDataBinding.chipSuccess.text =
            getString(R.string.bl_success) + " (" + viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.SUCCESS.action) + ")"
        mDataBinding.chipPending.text =
            getString(R.string.bl_pending) + " (" + viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.PENDING.action) + ")"
        mDataBinding.chipApproved.text =
            getString(R.string.bl_approved) + " (" + viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.APPROVED.action) + ")"
        mDataBinding.chipDanger.text =
            getString(R.string.bl_danger) + " (" + viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.DANGER.action) + ")"
        mDataBinding.chipRejected.text =
            getString(R.string.bl_rejected) + " (" +viewModel.getCountBalanceManageRecord(Constant.BalanceManagerStatus.REJECTED.action) + ")"

        if (mDataBinding.chipAllTransactions.isChecked) {
            getBalanceByFilterApi(Constant.BalanceManagerStatus.All.action)
        } else if (mDataBinding.chipSuccess.isChecked) {
            getBalanceByFilterApi(Constant.BalanceManagerStatus.SUCCESS.action)
        } else if (mDataBinding.chipPending.isChecked) {
            getBalanceByFilterApi(Constant.BalanceManagerStatus.PENDING.action)
        } else if (mDataBinding.chipApproved.isChecked) {
            getBalanceByFilterApi(Constant.BalanceManagerStatus.APPROVED.action)
        } else if (mDataBinding.chipDanger.isChecked) {
            getBalanceByFilterApi(Constant.BalanceManagerStatus.DANGER.action)
        } else if (mDataBinding.chipRejected.isChecked) {
            getBalanceByFilterApi(Constant.BalanceManagerStatus.REJECTED.action)
        }
    }
}