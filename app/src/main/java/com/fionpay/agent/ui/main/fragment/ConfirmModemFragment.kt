package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentConfirmModemBinding
import com.fionpay.agent.databinding.SuccessBottomSheetBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.ConfirmModemFragmentModule
import com.fionpay.agent.ui.main.di.DaggerConfirmModemFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class ConfirmModemFragment :
    BaseFragment<FragmentConfirmModemBinding>(R.layout.fragment_confirm_modem) {

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var dashBoardViewModelFactory: BaseViewModelFactory<DashBoardViewModel>
    private val viewModel: DashBoardViewModel by activityViewModels { dashBoardViewModelFactory }
    private var modemBalance = ""
    private var modemItemModel = ModemItemModel()
    lateinit var dialog: BottomSheetDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        getBundleData()
        initialization()
    }

    private fun initialization() {
        dialog = BottomSheetDialog(requireActivity())
        val fullName = "${modemItemModel.firstName} ${modemItemModel.lastName}"
        val modemBalance = "৳${modemBalance}"
        mDataBinding.topHeader.txtHeader.text = getString(R.string.confirm_modems)
        mDataBinding.txtName.text = fullName
        mDataBinding.txtBalanceAdded.text = modemBalance
        mDataBinding.txtPinCode.text = "${modemItemModel.pinCode}"
        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        mDataBinding.btnConfirmCreate.setOnClickListener {
            addModemItem()
        }
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            modemBalance = bundle.getString("modemBalance").toString()
            modemItemModel = bundle.getSerializable("modemItemModel") as ModemItemModel
        }
    }

    private fun initializeDagger() {
        DaggerConfirmModemFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .confirmModemFragmentModule(ConfirmModemFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun addModemItem() {

        if (networkHelper.isNetworkConnected()) {
            val modemItemModel = ModemItemModel(
                firstName = modemItemModel.firstName,
                lastName = modemItemModel.lastName,
                pinCode = modemItemModel.pinCode,
                balance = modemBalance.toDouble(),
                modemSlots = modemItemModel.modemSlots
            )
            viewModel.addModemItem(modemItemModel)
            viewModel.getAddModemItemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        val modemId = it.data?.modem?.id
                        //addModemBalance(modemId)
                        openBottomDialog()
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
            showMessage(mActivity.getString(R.string.NO_INTERNET_CONNECTION))
        }
    }

    private fun addModemBalance(modemId: String?) {
        if (networkHelper.isNetworkConnected()) {
            val addModemBalanceModel = AddModemBalanceModel(
                modemId = modemId.toString(),
                amount = modemBalance.toDouble(),
            )
            viewModel.addModemBalance(addModemBalanceModel)
            viewModel.getAddModemBalanceResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        openBottomDialog()
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        if (it.message == getString(R.string.invalid_access_token)) {
                            sessionExpired()
                        } else {
                            showSnackBar(
                                requireView(),
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
                requireView(),
                requireContext(),
                mActivity.getString(R.string.NO_INTERNET_CONNECTION)
            )
        }
    }

    private fun openBottomDialog() {
        val homeBottomSheetLayoutBinding = SuccessBottomSheetBinding.inflate(layoutInflater)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(homeBottomSheetLayoutBinding.root)
        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                dialog.dismiss()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_confirmModemFragment_to_navigation_modemFragment)
            }, 2000
        )
    }
}