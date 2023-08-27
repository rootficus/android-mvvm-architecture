package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.jionex.agent.R
import com.jionex.agent.data.model.request.GetMessageByFilterRequest
import com.jionex.agent.data.model.response.GetMessageManageRecord
import com.jionex.agent.databinding.FragmentSmsInboxBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.adapter.BleManagerListAdapter
import com.jionex.agent.ui.main.adapter.SmsManagerListAdapter
import com.jionex.agent.ui.main.di.DaggerSMSInboxFragmentComponent
import com.jionex.agent.ui.main.di.SMSInboxFragmentModule
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.Constant
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class SMSInboxFragment : BaseFragment<FragmentSmsInboxBinding>(R.layout.fragment_sms_inbox) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private var smsManagerListAdapter: SmsManagerListAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView()

    }

    private fun initializeDagger() {
        DaggerSMSInboxFragmentComponent.builder().appComponent(JionexSDK.appComponent)
            .sMSInboxFragmentModule(SMSInboxFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        mDataBinding.chipAllSMS.isClickable = true
        setMessageAdapter(-1)
        setMessageFilterCount()
        mDataBinding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.forEach {
                when (it) {
                    mDataBinding.chipAllSMS.id -> {
                        getMessageByFilterApi(Constant.SMSType.All.value)
                    }

                    mDataBinding.chipCashIn.id -> {
                        getMessageByFilterApi(Constant.SMSType.CashIn.value)
                    }

                    mDataBinding.chipCashOut.id -> {
                        getMessageByFilterApi(Constant.SMSType.CashOut.value)
                    }

                    mDataBinding.chipB2B.id -> {
                        getMessageByFilterApi(Constant.SMSType.B2B.value)
                    }
                    mDataBinding.chipUnKnown.id -> {
                        getMessageByFilterApi(Constant.SMSType.UNKNOWN.value)
                    }
                }
            }
        }

        mDataBinding.refreshLinear.setOnClickListener {
            if (networkHelper.isNetworkConnected()) {
                viewModel.deleteLocalMessageManager()
                getMessageByFilterApi(Constant.SMSType.All.value)
                setMessageFilterCount()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.NO_INTERNET_CONNECTION),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }
    private fun getMessageByFilterApi(filter: Int) {

        if (networkHelper.isNetworkConnected()) {
            val getMessageByFilterRequest = GetMessageByFilterRequest(
                page_size = 50,
                page_number = 1,
                /* sender = "",
                 agent_account_no = 0,
                 transaction_id = "",
                 type = "",
                 from = "",
                 to = "",*/
                message_type = filter

            )
            viewModel.getMessageByFilter(
                getMessageByFilterRequest
            )
            viewModel.getMessageManageRecordModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        setMessageAdapter(filter)
                    }

                    Status.ERROR -> {
                        //startActivity(Intent(requireContext(), SignInActivity::class.java))
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
        } else {
            progressBar.dismiss()
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private fun setMessageAdapter(filter:Int) {
        smsManagerListAdapter?.notifyDataSetChanged()
        var arrayList = viewModel.getMessageManageRecord(filter)
        Log.d("getBalanceByFilterApi", "::${filter},${arrayList.size}")
        smsManagerListAdapter = SmsManagerListAdapter(arrayList)
        //smsManagerListAdapter?.listener = cardListener
        mDataBinding.recentFilteredMessage.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentFilteredMessage.adapter = smsManagerListAdapter
        smsManagerListAdapter?.notifyDataSetChanged()
        if (smsManagerListAdapter?.itemCount!! >0){
            mDataBinding.textNoTransactions.visibility = View.INVISIBLE
            mDataBinding.recentFilteredMessage.visibility = View.VISIBLE
        }else{
            mDataBinding.recentFilteredMessage.visibility = View.INVISIBLE
            mDataBinding.textNoTransactions.visibility = View.VISIBLE
        }
        setMessageCountValue()
    }

    private fun setMessageFilterCount() {
        setMessageCountValue()
        if (mDataBinding.chipAllSMS.isChecked) {
            getMessageByFilterApi(Constant.SMSType.All.value)
        } else if (mDataBinding.chipCashIn.isChecked) {
            getMessageByFilterApi(Constant.SMSType.CashIn.value)
        } else if (mDataBinding.chipCashOut.isChecked) {
            getMessageByFilterApi(Constant.SMSType.CashOut.value)
        } else if (mDataBinding.chipB2B.isChecked) {
            getMessageByFilterApi(Constant.SMSType.B2B.value)
        } else if (mDataBinding.chipUnKnown.isChecked) {
            getMessageByFilterApi(Constant.SMSType.UNKNOWN.value)
        }
    }

    private fun setMessageCountValue() {
        var totalCount = viewModel.getCountMessageManageRecord(Constant.SMSType.All.value);
        var cashInCount = viewModel.getCountMessageManageRecord(Constant.SMSType.CashIn.value);
        var cashOutCount = viewModel.getCountMessageManageRecord(Constant.SMSType.CashOut.value);
        var b2b = viewModel.getCountMessageManageRecord(Constant.SMSType.B2B.value)
        var unKnown = viewModel.getCountMessageManageRecord(Constant.SMSType.UNKNOWN.value)
        mDataBinding.chipAllSMS.text = getString(R.string.sms_all_sms) + " (" + totalCount + ")"
        mDataBinding.chipCashIn.text = getString(R.string.cash_in) + " (" + cashInCount + ")"
        mDataBinding.chipCashOut.text = getString(R.string.cash_out) + " (" + cashOutCount + ")"
        mDataBinding.chipB2B.text = getString(R.string.sms_b2b) + " (" + b2b + ")"
        mDataBinding.chipUnKnown.text = getString(R.string.sms_unknown) + " (" + unKnown + ")"
    }

}