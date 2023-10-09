package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.AddModemBalanceModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {
        getBundleData()
        mDataBinding.topHeader.txtHeader.text = getString(R.string.add_modems)
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
                addModemBalance()
            }
        }
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            modemId = bundle.getString("modem_id").toString()
        }
    }

    private fun initializeDagger() {
        DaggerAddModemBalanceFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .addModemBalanceFragmentModule(AddModemBalanceFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun addModemBalance() {
        if (networkHelper.isNetworkConnected()) {
            val addModemBalanceModel = AddModemBalanceModel(
                modemId,
                mDataBinding.etAddBalance.text.toString(),
            )
            viewModel.addModemBalance(addModemBalanceModel)
            viewModel.getAddModemBalanceResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_navigation_addModemBalanceFragment_to_navigation_confirmModemFragment)
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        if (it.message == "Invalid access token") {
                            sessionExpired()
                        } else {
                            showSnackBar(
                                mDataBinding.mainLayout,
                                requireContext(),
                                "Amount should be less the Agent balance"
                            )
                        }
                    }

                    Status.LOADING -> {
                        progressBar.show()
                    }
                }
            }
        } else {
            progressBar.dismiss()
            showSnackBar(
                mDataBinding.mainLayout,
                requireContext(),
                mActivity.getString(R.string.NO_INTERNET_CONNECTION)
            )
        }
    }
}