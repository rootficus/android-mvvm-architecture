package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.GetModemsByFilterRequest
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsByFilterResponse
import com.fionpay.agent.databinding.FragmentModemBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.DashBoardActivity
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.ModemsManagerListAdapter
import com.fionpay.agent.ui.main.adapter.SmsManagerListAdapter
import com.fionpay.agent.ui.main.di.DaggerModemFragmentComponent
import com.fionpay.agent.ui.main.di.ModemFragmentModule
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import javax.inject.Inject


class ModemFragment : BaseFragment<FragmentModemBinding>(R.layout.fragment_modem) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }

    private var modemsManagerListAdapter: ModemsManagerListAdapter? = null
    private var listGetModemsByFilter : ArrayList<GetModemsByFilterResponse> = arrayListOf()

    private var apiCall : String = ""
    private var filter = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView()

    }

    private fun initializeDagger() {
        DaggerModemFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .modemFragmentModule(ModemFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView() {
        getBundleData()
        mDataBinding.addModemButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.navigation_addModemFragment)
        }
        getModemsByFilterApi()
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            apiCall = bundle.getString("Api").toString()
            filter = bundle.getInt("Filer")
        }
    }


    private fun getModemsByFilterApi() {

        if (networkHelper.isNetworkConnected()) {
            val getModemsByFilterRequest = GetModemsByFilterRequest(
                page_size = 50,
                page_number = 1,
                /* sender = "",
                 agent_account_no = 0,
                 transaction_id = "",
                 type = "",
                 from = "",
                 to = "",*/
                is_active = true

            )
            viewModel.getModemsByFilter(
                getModemsByFilterRequest
            )
            viewModel.getModemsByFilterResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data","::${it.data}")
                        it.data?.let { it1 -> listGetModemsByFilter.addAll(it1) }
                        setModemsAdapter()
                        // bleManagerListAdapter?.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                       // startActivity(Intent(requireContext(), SignInActivity::class.java))
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

    private fun setModemsAdapter() {
        modemsManagerListAdapter = ModemsManagerListAdapter(listGetModemsByFilter)
        mDataBinding.recentModemsList.layoutManager = LinearLayoutManager(context)
        modemsManagerListAdapter?.listener = cardListener
        mDataBinding.recentModemsList.adapter = modemsManagerListAdapter
    }

    private val cardListener = object : ModemsManagerListAdapter.ModemCardEvent {
        override fun onCardClicked(getModemsByFilterResponse: GetModemsByFilterResponse) {
            val bottomSheetFragment = ModemDetailScreenFragment()
            bottomSheetFragment.listener = modemDetailScreenActionListener
            val bundle = Bundle()
            bundle.putSerializable(GetMessageManageRecord::class.java.name, getModemsByFilterResponse)
            bottomSheetFragment.arguments = bundle
            activity?.supportFragmentManager?.let {
                bottomSheetFragment.show(
                    it,
                    "ActionBottomDialogFragment"
                )
            }
            //showCustomDialog(getBalanceManageRecord)
        }
    }

    private val modemDetailScreenActionListener = object : ModemDetailScreenFragment.BottomDialogEvent {
        override fun onAcceptRequest(getModemsByFilterResponse: GetModemsByFilterResponse) {

        }

        override fun onRejectedRequest(getModemsByFilterResponse: GetModemsByFilterResponse) {
            TODO("Not yet implemented")
        }

    }


}