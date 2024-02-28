package com.rf.utellRestaurant.ui.main.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.data.model.request.SignInRequest
import com.rf.utellRestaurant.data.model.request.UserRequest
import com.rf.utellRestaurant.databinding.FragmentSignInBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseFragment
import com.rf.utellRestaurant.ui.base.BaseFragmentModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.activity.DashBoardActivity
import com.rf.utellRestaurant.ui.main.di.DaggerSignInFragmentComponent
import com.rf.utellRestaurant.ui.main.di.DashBoardFragmentModuleDi
import com.rf.utellRestaurant.ui.main.di.SignInFragmentModule
import com.rf.utellRestaurant.ui.main.di.SignInFragmentModuleDi
import com.rf.utellRestaurant.ui.main.viewmodel.SignInViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import com.rf.utellRestaurant.utils.Status
import com.rf.utellRestaurant.utils.Utility
import com.rf.utellRestaurant.utils.Utility.isEmailValid
import javax.inject.Inject


class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private var name: String = ""
    private var password: String = ""

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var progressBar: Dialog

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by activityViewModels { signInViewModelFactory }
    private var isPasswordVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView(view)

    }

    private fun initializeDagger() {

        DaggerSignInFragmentComponent.builder().appComponent(UtellSDK.appComponent)
            .signInFragmentModuleDi(SignInFragmentModuleDi())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(view: View) {

        mDataBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

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

        mDataBinding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        mDataBinding.passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        mDataBinding.etPassword.addTextChangedListener(object : TextWatcher {
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
        })

        mDataBinding.btnSignIn.setOnClickListener {
            //startActivity(Intent(requireContext(), DashBoardActivity::class.java))
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
            } else {
                callSignInAPI(view)
            }
        }
        mDataBinding.etName.setText(viewmodel.getEmail().toString())
        mDataBinding.etPassword.setText(viewmodel.getPassword().toString())

    }

    private fun callSignInAPI(view: View) {

        if (networkHelper.isNetworkConnected()) {
            viewmodel.signInNow(
                SignInRequest( UserRequest(
                    email = name,
                    password = password,
                )
                )
            )
            viewmodel.signInResponseModel.observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.dismiss()
                        startActivity(Intent(requireContext(), DashBoardActivity::class.java))

               /*         val bundle = Bundle()
                        bundle.putString("password", password)
                        viewmodel.setPassword(password)
                        viewmodel.setToken(it.data?.token.toString())
                        viewmodel.setEmail(mDataBinding.etName.text.toString())
                        viewmodel.setFullName(it.data?.fullName)
                        viewmodel.setPhoneNumber(it.data?.phone)
                        viewmodel.setUserId(it.data?.id)*/
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
    }
}