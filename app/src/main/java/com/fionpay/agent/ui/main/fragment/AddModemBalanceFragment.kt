package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.databinding.FragmentAddModemBalanceBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.di.AddModemBalanceFragmentModule
import com.fionpay.agent.ui.main.di.DaggerAddModemBalanceFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
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
    private var modemItemModel = ModemItemModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {
        getBundleData()
        mDataBinding.topHeader.txtHeader.text = getString(R.string.add_modems)
        mDataBinding.txtExistingBalance.text = viewModel.getAvailableBalance().toString()
        mDataBinding.labelTotalBalance.text = getString(R.string.new_balance_will, viewModel.getAvailableBalance().toString())
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
                    val totalBalance = viewModel.getAvailableBalance().minus(newVal)
                    if (totalBalance > 0.0) {
                        mDataBinding.labelTotalBalance.text = getString(R.string.new_balance_will, totalBalance.toString())
                    } else {
                        showMessage("Entered amount should be less than agent balance")
                        mDataBinding.labelTotalBalance.text = getString(R.string.new_balance_will, viewModel.getAvailableBalance().toString())
                        mDataBinding.etAddBalance.setText("")
                    }

                } else {
                    mDataBinding.labelTotalBalance.text =
                        getString(R.string.new_balance_will, viewModel.getAvailableBalance().toString())
                }

            }
        })

        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }

        mDataBinding.btnNext.setOnClickListener {
            if (mDataBinding.etAddBalance.text.toString().isEmpty()) {
                Utility.callCustomToast(requireContext(), mActivity.getString(R.string.PLEASE_ENTER_BALANCE))
            } else {
                val bundle = Bundle().apply {
                    putString("modemBalance", mDataBinding.etAddBalance.text.toString())
                    putSerializable("modemItemModel", modemItemModel)
                }
                Navigation.findNavController(requireView())
                    .navigate(
                        R.id.action_navigation_addModemBalanceFragment_to_navigation_confirmModemFragment,
                        bundle
                    )
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