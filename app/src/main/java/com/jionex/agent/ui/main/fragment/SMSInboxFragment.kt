package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jionex.agent.R
import com.jionex.agent.data.model.request.GetMessageByFilterRequest
import com.jionex.agent.data.model.response.GetMessageByFilterResponse
import com.jionex.agent.databinding.FragmentSignInBinding
import com.jionex.agent.databinding.FragmentSmsInboxBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.activity.SignInActivity
import com.jionex.agent.ui.main.adapter.SmsManagerListAdapter
import com.jionex.agent.ui.main.di.DaggerSMSInboxFragmentComponent
import com.jionex.agent.ui.main.di.SMSInboxFragmentModule
import com.jionex.agent.ui.main.viewmodel.DashBoardViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
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

    private var listGetMessageByFilter : ArrayList<GetMessageByFilterResponse> = arrayListOf()

    private var apiCall : String = ""
    private var filter = 0


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
        getBundleData()
        getMessageByFilterApi()
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            apiCall = bundle.getString("Api").toString()
            filter = bundle.getInt("Filer")
        }
    }
    private fun getMessageByFilterApi() {

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
            viewModel.getMessageByFilterResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data","::${it.data}")
                        it.data?.let { it1 -> listGetMessageByFilter.addAll(it1) }
                        setMessageAdapter()
                        // bleManagerListAdapter?.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        startActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()

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

    private fun setMessageAdapter() {
        smsManagerListAdapter = SmsManagerListAdapter(listGetMessageByFilter)
        mDataBinding.recentFilteredMessage.layoutManager = LinearLayoutManager(context)
        mDataBinding.recentFilteredMessage.adapter = smsManagerListAdapter
    }

}