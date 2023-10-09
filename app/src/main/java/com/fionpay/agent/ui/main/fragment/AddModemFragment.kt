package com.fionpay.agent.ui.main.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.databinding.FragmentAddModemBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.adapter.DashBoardListAdapter
import com.fionpay.agent.ui.main.di.AddModemFragmentModule
import com.fionpay.agent.ui.main.di.DaggerAddModemFragmentComponent
import com.fionpay.agent.ui.main.viewmodel.DashBoardViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.fionpay.agent.utils.Utility
import javax.inject.Inject


class AddModemFragment : BaseFragment<FragmentAddModemBinding>(R.layout.fragment_add_modem) {

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
    val otpStringBuilder = StringBuilder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDagger()
        initialization()
    }

    private fun initialization() {
        mDataBinding.topHeader.txtHeader.text = getString(R.string.add_modems)

        mDataBinding.btnNext.setOnClickListener {
            if (mDataBinding.etName.text.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_NAME)
                )
                //mDataBinding.etName.error = mActivity.getString(R.string.PLEASE_ENTER_NAME)
            } else if (mDataBinding.etLastName.text.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_LAST_NAME)
                )
                //mDataBinding.etPhoneNumber.error = mActivity.getString(R.string.PLEASE_ENTER_PHONE_NUMBER)
            } else if (otpStringBuilder.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_PIN)
                )
            } else {
                addModemItem()
            }

        }

        mDataBinding.topHeader.backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }

        val otpBoxes = arrayOf(
            mDataBinding.otpLayout.otpBox1,
            mDataBinding.otpLayout.otpBox2,
            mDataBinding.otpLayout.otpBox3,
            mDataBinding.otpLayout.otpBox4,
            mDataBinding.otpLayout.otpBox5,
            mDataBinding.otpLayout.otpBox6
        )

        // Set up text change listeners for each OTP box
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        // When a digit is entered, move focus to the next OTP box
                        if (i < otpBoxes.size - 1) {
                            otpBoxes[i + 1].requestFocus()
                        } else {
                            // If this is the last OTP box, you can perform some action like submitting the OTP
                            // For example: validateOTP()
                        }
                    } else if (s?.isEmpty() == true && i > 0) {
                        // When the user clears the digit, move focus to the previous OTP box
                        otpBoxes[i - 1].requestFocus()
                    }
                    s?.toString()?.let { otpStringBuilder.append(it) }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

    }

    private fun initializeDagger() {
        DaggerAddModemFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .addModemFragmentModule(AddModemFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun addModemItem() {

        if (networkHelper.isNetworkConnected()) {
            val modemItemModel = ModemItemModel(
                mDataBinding.etName.text.toString(),
                mDataBinding.etLastName.text.toString(),
                otpStringBuilder.toString().toLong()
            )
            viewModel.addModemItem(modemItemModel)
            viewModel.getAddModemItemResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        Log.i("Data", "::${it.data}")
                        val modemId = it.data?.id
                        val bundle = Bundle().apply {
                            putString("modem_id", modemId)
                        }
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_navigation_addModemFragment_to_navigation_addModemBalanceFragment, bundle)
                        // bleManagerListAdapter?.notifyDataSetChanged()
                    }

                    Status.ERROR -> {
                        // startActivity(Intent(requireContext(), SignInActivity::class.java))
                        progressBar.dismiss()
                        if (it.message == "Invalid access token") {
                            sessionExpired()
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
}