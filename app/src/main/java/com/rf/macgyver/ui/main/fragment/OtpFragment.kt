package com.rf.macgyver.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.rf.macgyver.R
import com.rf.macgyver.databinding.FragmentOtpBinding
import com.rf.macgyver.ui.base.BaseFragment
import com.rf.macgyver.ui.main.activity.DashBoardActivity
import com.rf.macgyver.utils.Utility
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OtpFragment : BaseFragment<FragmentOtpBinding>(R.layout.fragment_otp) {
    private val TAG = OtpFragment::class.java.name

    private lateinit var mAuth: FirebaseAuth
    lateinit var binding: FragmentOtpBinding
    private var verificationId: String= ""
    @Inject
    lateinit var progressBar: Dialog

    val otpStringBuilder = StringBuilder()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        initializeDagger()*/

        initializeView(view)
        progressBar = Dialog(requireContext())
        progressBar.setContentView(R.layout.item_progress_bar)
    }

    private fun initializeView(view: View): View {


        mAuth = FirebaseAuth.getInstance()


        val phone = arguments?.getString("phone")
        mDataBinding.phoneNoTxt.text = phone
        Log.d(TAG,"phone${phone}")
        if (phone != null) {
            startPhoneNumberVerification(phone)
        }
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
                mDataBinding.timerText.text = timeLeftFormatted
            }

            override fun onFinish() {
                mDataBinding.timerText.text = "00:00"
            }
        }.start()

        val otpBoxes = arrayOf(
            mDataBinding.etOtp.otpBox1,
            mDataBinding.etOtp.otpBox2,
            mDataBinding.etOtp.otpBox3,
            mDataBinding.etOtp.otpBox4,
            mDataBinding.etOtp.otpBox5,
            mDataBinding.etOtp.otpBox6
        )
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
                            // otp = s.toString()
                        }
                    } else if (s?.isEmpty() == true && i > 0) {
                        otpBoxes[i - 1].requestFocus()
                        if (otpStringBuilder.isNotEmpty()) {
                            otpStringBuilder.deleteCharAt(i)
                        }
                    } else if (s?.isEmpty() == true) {
                        if (otpStringBuilder.isNotEmpty()) {
                            otpStringBuilder.clear()
                        }
                    }

                    s?.toString()?.let { otpStringBuilder.append(it) }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }

        mDataBinding.btnValidate.setOnClickListener {
             if (verificationId.isNullOrBlank()){
                Utility.callCustomToast(
                    requireContext(),"Issue with firebase"
                )
            }
            else if (otpStringBuilder.toString().isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_PIN)
                )
            } else {
                val credential =
                    PhoneAuthProvider.getCredential(verificationId, otpStringBuilder.toString())
                signInWithPhoneAuthCredential(credential)
            }
        }
        return view
    }


    private fun startPhoneNumberVerification(phoneNumber: String) {

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallBack)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            // below method is used when
            // OTP is sent from Firebase
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode

                progressBar.show()
                Toast.makeText(requireContext(), "$code", Toast.LENGTH_SHORT).show()
                if (code != null) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {

                progressBar.show()
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show()
                    selectValidate()
                } else {
                    Toast.makeText(
                        context,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun selectValidate() {
        val intent = Intent(requireActivity(), DashBoardActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}


