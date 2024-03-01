package com.rf.baseSideNav.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.rf.baseSideNav.R
import com.rf.baseSideNav.databinding.FragmentSignInBinding
import com.rf.baseSideNav.ui.base.BaseFragment
import com.rf.baseSideNav.utils.NetworkHelper
import com.rf.baseSideNav.utils.SharedPreference
import javax.inject.Inject
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    /* @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by activityViewModels { signInViewModelFactory }
    private var isPasswordVisible = false*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        initializeDagger()*/
        initializeView(view)

    }

    /* private fun initializeDagger() {

        DaggerSignInFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .signInFragmentModuleDi(SignInFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }*/

    private fun initializeView(view: View): View {

        binding = FragmentSignInBinding.inflate(layoutInflater)

        mDataBinding.etPhoneNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {

                    mDataBinding.btnSignIn.isEnabled = true
                }
            }
        })

        mDataBinding.btnSignIn.setOnClickListener {
            val phone = "+91" + mDataBinding.etPhoneNo.text.toString()
            val bundle = Bundle().apply {
                putString("phone", phone)
            }
            findNavController().navigate(R.id.action_fragment_SignIn_to_fragment_Otp,bundle)


        }
        /*mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // Automatically handle the verification if the phone number is instantly verified
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, "Verification Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
                // Move to OTP fragment
                findNavController().navigate(R.id.action_fragment_SignIn_to_fragment_Otp)
            }
        }*/

        return binding.root    }

    /*private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            mCallbacks
        )
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }*/
}



        /*mDataBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()

                if (isEmailValid(email)) {
                    mDataBinding.emailVerified.visibility = View.VISIBLE
                } else {
                    mDataBinding.emailVerified.visibility = View.GONE
                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString()
                        .isNotEmpty() && mDataBinding.etPassword.text!!.isNotEmpty() && mDataBinding.etPassword.text!!.length > 8
                ) {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.btn_active_back)
                    mDataBinding.btnSignIn.isEnabled = true
                } else {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(requireContext(),R.drawable.btn_inactive_back)
                    mDataBinding.btnSignIn.isEnabled = true
                }
            }
        })

        mDataBinding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()*/

        /*mDataBinding.passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }*/

       /* mDataBinding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString()
                        .isNotEmpty() && s.toString().length > 8 && mDataBinding.etName.text!!.toString()
                        .isNotEmpty()
                ) {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.btn_active_back)
                    mDataBinding.btnSignIn.isEnabled = true
                } else {
                    mDataBinding.btnSignIn.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.btn_inactive_back)
                    mDataBinding.btnSignIn.isEnabled = true
                }

            }
        })*/

       /* mDataBinding.btnSignIn.setOnClickListener {
            startActivity(Intent(requireContext(), DashBoardActivity::class.java))
            name = mDataBinding.etName.text.toString()
            password = mDataBinding.etPassword.text.toString()
            if (name.isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_NAME)
                )
            } else if (password.isEmpty()) {
                Utility.callCustomToast(
                    requireContext(),
                    mActivity.getString(R.string.PLEASE_ENTER_PASSWORD)
                )
            }*//* else {
                callSignInAPI(view)
            }*/

        /*mDataBinding.etName.setText(viewmodel.getEmail().toString())
        mDataBinding.etPassword.setText(viewmodel.getPassword().toString())*/



    /*private fun callSignInAPI(view: View) {

        if (networkHelper.isNetworkConnected()) {
            viewmodel.signInNow(
                SignInRequest(
                    email = name,
                    password = password,
                    deviceToken = sharedPreference.getPushToken().toString()
                )
            )
            viewmodel.signInResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()

                        val bundle = Bundle()
                        bundle.putString("password", password)
                        viewmodel.setPassword(password)
                        viewmodel.setToken(it.data?.token.toString())
                        viewmodel.setEmail(mDataBinding.etName.text.toString())
                        viewmodel.setFullName(it.data?.fullName)
                        viewmodel.setPhoneNumber(it.data?.phone)
                        viewmodel.setUserId(it.data?.id)
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

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Password is currently visible, so hide it
            mDataBinding.etPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
            mDataBinding.passwordToggle.setImageResource(R.drawable.show_password)
        } else {
            // Password is currently hidden, so make it visible
            mDataBinding.etPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            mDataBinding.passwordToggle.setImageResource(R.drawable.hide_password)
        }

        // Move the cursor to the end of the text
        mDataBinding.etPassword.setSelection(mDataBinding.etPassword.text!!.length)
        // Toggle the visibility flag
        isPasswordVisible = !isPasswordVisible
    }*/
