package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.jionex.agent.R
import com.jionex.agent.data.model.request.VerifyPinRequest
import com.jionex.agent.databinding.FragmentVerifyPinBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.activity.DashBoardActivity
import com.jionex.agent.ui.main.activity.SignInActivity
import com.jionex.agent.ui.main.di.DaggerVerifyPinFragmentComponent
import com.jionex.agent.ui.main.di.VerifyPinFragmentModule
import com.jionex.agent.ui.main.viewmodel.SignInViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()
        initializeView(view)
    }

    private fun initDagger() {
        DaggerVerifyPinFragmentComponent.builder().appComponent(JionexSDK.appComponent)
            .verifyPinFragmentModule(VerifyPinFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(view: View) {
        if (arguments != null) {
            dialCode = arguments?.getString("dialCode").toString()
            contactNumber = arguments?.getString("mobile").toString()
            Log.d("dialcode string", dialCode)
        }

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

        mDataBinding.btnVerifyButton.setOnClickListener {
            val otp = mDataBinding.editTextPin.text.toString()
            if (otp.isEmpty()) {
                callCustomToast(mActivity.getString(R.string.PLEASE_ENTER_PIN))
            }else {
                callPinVerification()
            }

        }
        mDataBinding.editTextPin.setText("583543")
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