package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.GetBalanceByFilterRequest
import com.fionpay.agent.data.model.request.UpdateBalanceRequest
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.databinding.CustomBleDialogBinding
import com.fionpay.agent.databinding.FragmentBlManagerBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.BleManagerListAdapter
import com.fionpay.agent.ui.main.di.BLManagerFragmentModule
import com.fionpay.agent.ui.main.di.DaggerBLManagerFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.Constant
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
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
        DaggerBLManagerFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .bLManagerFragmentModule(BLManagerFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        mDataBinding.chipAllTransactions.isClickable = true
        setBleAdapter(-1)
        setBleFilterCount()
        getStatusCount()
        mDataBinding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.forEach {
                when (it) {
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

                    mDataBinding.chipApproved.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.APPROVED.action)
                    }

                    mDataBinding.chipDanger.id -> {
                        getBalanceByFilterApi(Constant.BalanceManagerStatus.DANGER.action)
                    }
                }
            }
        }

        mDataBinding.refreshLinear.setOnClickListener {
            if (networkHelper.isNetworkConnected()) {
                viewModel.deleteLocalBlManager()
                initializeView()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.NO_INTERNET_CONNECTION),
                    Toast.LENGTH_SHORT
                ).show()
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
        if (bleManagerListAdapter?.itemCount!! >0){
            mDataBinding.textNoTransactions.visibility = View.INVISIBLE
            mDataBinding.recentTrendingView.visibility = View.VISIBLE
        }else{
            mDataBinding.recentTrendingView.visibility = View.INVISIBLE
            mDataBinding.textNoTransactions.visibility = View.VISIBLE
        }
    }

    private val cardListener = object : BleManagerListAdapter.CardEvent {
        override fun onCardClicked(getBalanceManageRecord: GetBalanceManageRecord) {
            var bottomSheetFragment = BalanceDetailScreenFragment()
            bottomSheetFragment.listener = balanceDetailScreenActionListener
            val bundle = Bundle()
            bundle.putSerializable(GetBalanceManageRecord::class.java.name,getBalanceManageRecord)
            bottomSheetFragment.arguments = bundle
            activity?.supportFragmentManager?.let { bottomSheetFragment.show(it, "ActionBottomDialogFragment") }
            //showCustomDialog(getBalanceManageRecord)
        }
    }

    private val balanceDetailScreenActionListener = object : BalanceDetailScreenFragment.BottomDialogEvent {
        override fun onAcceptRequest(getBalanceManageRecord: GetBalanceManageRecord) {
            getBalanceManageRecord.status = "approved"
            onUpdateRequest(getBalanceManageRecord,UpdateBalanceRequest("approved",getBalanceManageRecord.id,viewModel.getUserId()!!),
                getString(R.string.you_want_to_approved))
        }

        override fun onRejectedRequest(getBalanceManageRecord: GetBalanceManageRecord) {
            onUpdateRequest(getBalanceManageRecord,UpdateBalanceRequest("rejected",getBalanceManageRecord.id,viewModel.getUserId()!!),
                getString(R.string.you_want_to_rejected))
        }

    }

    private fun onUpdateRequest(getBalanceManageRecord: GetBalanceManageRecord,updateBalanceRequest: UpdateBalanceRequest,message: String) {
        val mBuilder = android.app.AlertDialog.Builder(activity)
            .setTitle("Are you sure?")
            .setMessage(message)
            .setPositiveButton("Yes", null)
            .setNegativeButton("No", null)
            .show()
        val mPositiveButton = mBuilder.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            mBuilder.dismiss()
            updateBLStatusApi(getBalanceManageRecord,updateBalanceRequest)
        }
    }

    private fun updateBLStatusApi(getBalanceManageRecord: GetBalanceManageRecord, updateBalanceRequest: UpdateBalanceRequest){
        //Api calling
        if (networkHelper.isNetworkConnected()){
            Log.d("updateBalanceRequest","::${Gson().toJson(updateBalanceRequest)}")
            viewModel.updateBLStatus(updateBalanceRequest)
            viewModel.updateBLStatusResponseModel.observe(viewLifecycleOwner) {
                when(it.status){
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        it.data?.let {
                            viewModel.updateLocalBalanceManager(getBalanceManageRecord)
                            bleManagerListAdapter?.notifyDataSetChanged()
                        }
                        Toast.makeText(activity,"Status updated successfully", Toast.LENGTH_SHORT).show()
                        getStatusCount()
                    }
                    Status.ERROR -> {
                        progressBar.dismiss()
                        if(it.message == "Invalid access token"){
                            Toast.makeText(activity,"Invalid access token", Toast.LENGTH_SHORT).show()
                        }
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        }else{
            Toast.makeText(activity,getString(R.string.NO_INTERNET_CONNECTION),Toast.LENGTH_SHORT).show()
        }
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
                        if(it.message == "Invalid access token"){
                            Toast.makeText(activity,"Invalid access token", Toast.LENGTH_SHORT).show()
                        }
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        }else{
            Toast.makeText(activity,getString(R.string.NO_INTERNET_CONNECTION),Toast.LENGTH_SHORT).show()
        }
    }

    private fun getStatusCount() {

        if (networkHelper.isNetworkConnected()) {
            viewModel.getStatusCount()
            viewModel.getStatusCountResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
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
                        progressBar.dismiss()
                        if(it.message == "Invalid access token"){
                            Toast.makeText(activity,"Invalid access token",Toast.LENGTH_SHORT).show()
                        }
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            showMessage(getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private fun setBleFilterCount() {
        var totalCount = viewModel.getBLSuccess() + viewModel.getBLPending() +
                viewModel.getBLApproved() + viewModel.getBLDanger()+
                viewModel.getBLRejected()
        mDataBinding.chipAllTransactions.text = getString(R.string.bl_all_transactions) + " (" + totalCount + ")"
        mDataBinding.chipSuccess.text = getString(R.string.bl_success) + " (" + viewModel.getBLSuccess() + ")"
        mDataBinding.chipPending.text = getString(R.string.bl_pending) + " (" + viewModel.getBLPending() + ")"
        mDataBinding.chipApproved.text = getString(R.string.bl_approved) + " (" + viewModel.getBLApproved() + ")"
        mDataBinding.chipDanger.text = getString(R.string.bl_danger) + " (" + viewModel.getBLDanger() + ")"
        mDataBinding.chipRejected.text = getString(R.string.bl_rejected) + " (" +viewModel.getBLRejected() + ")"
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