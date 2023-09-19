package com.fionpay.agent.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.fionpay.agent.R
import com.fionpay.agent.data.model.request.VerifyPinRequest
import com.fionpay.agent.databinding.FragmentVerifyPinBinding
import com.fionpay.agent.databinding.SuccessBottomSheetBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseFragment
import com.fionpay.agent.ui.base.BaseFragmentModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.activity.DashBoardActivity
import com.fionpay.agent.ui.main.activity.SignInActivity
import com.fionpay.agent.ui.main.di.DaggerVerifyPinFragmentComponent
import com.fionpay.agent.ui.main.di.VerifyPinFragmentModule
import com.fionpay.agent.ui.main.viewmodel.SignInViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class VerifyPinFragment :
    BaseFragment<FragmentVerifyPinBinding>(R.layout.fragment_verify_pin) {

    private var dialCode: String = ""
    private var contactNumber: String = ""

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by activityViewModels { signInViewModelFactory }
    lateinit var dialog: BottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()
        initializeView(view)
    }

    private fun initDagger() {
        DaggerVerifyPinFragmentComponent.builder().appComponent(FionSDK.appComponent)
            .verifyPinFragmentModule(VerifyPinFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(view: View) {
        if (arguments != null) {
            dialCode = arguments?.getString("dialCode").toString()
            contactNumber = arguments?.getString("mobile").toString()
            Log.d("dialcode string", dialCode)
        }
        dialog = BottomSheetDialog(requireActivity())

        mDataBinding.editTextPin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length >= 6) {
                    mDataBinding.btnVerifyButton.background =
                        mActivity.getDrawable(R.drawable.btn_active_back)
                    mDataBinding.btnVerifyButton.isEnabled = true
                } else {
                    mDataBinding.btnVerifyButton.background =
                        mActivity.getDrawable(R.drawable.btn_inactive_back)
                    mDataBinding.btnVerifyButton.isEnabled = true
                }
            }

        })

        mDataBinding.back.setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }

        val otpBoxes = arrayOf(mDataBinding.otpLayout.otpBox1, mDataBinding.otpLayout.otpBox2, mDataBinding.otpLayout.otpBox3, mDataBinding.otpLayout.otpBox4,
            mDataBinding.otpLayout.otpBox5, mDataBinding.otpLayout.otpBox6)

        // Set up text change listeners for each OTP box
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        mDataBinding.btnVerifyButton.setOnClickListener {
            val otp = mDataBinding.editTextPin.text.toString()
            openBottomDialog()
            if (otp.isEmpty()) {
                callCustomToast(mActivity.getString(R.string.PLEASE_ENTER_PIN))
            }else {
                callPinVerification()
            }

        }
      /*  mDataBinding.editTextPin.setText("224951")*/
    }

    private fun openBottomDialog() {
        val homeBottomSheetLayoutBinding = SuccessBottomSheetBinding.inflate(layoutInflater)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(homeBottomSheetLayoutBinding.root)
        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                goToNextScreen()
            }, 2000
        )
    }

   private fun callPinVerification() {
        Log.d("dialcode string", mDataBinding.editTextPin.toString())

        if (networkHelper.isNetworkConnected()) {
            viewmodel.verifyUserByPinCode(
                VerifyPinRequest(viewmodel.getUserId().toString(),mDataBinding.editTextPin.text.toString())
            )
            viewmodel.verifyUserByPinCodeResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        val message = "The verification code is correct."
                        viewmodel.setIsLogin(true)
                        viewmodel.setPinCode(mDataBinding.editTextPin.text.toString().toInt())
                        showMessage(message)
                        goToNextScreen()
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        callCustomToast(it.message.toString())
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

    private fun goToNextScreen() {
        startActivity(Intent(activity, DashBoardActivity::class.java))
        activity?.finishAffinity()
    }

    private fun callCustomToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }

}