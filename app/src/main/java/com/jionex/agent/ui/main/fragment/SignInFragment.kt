package com.jionex.agent.ui.main.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.jionex.agent.R
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.databinding.FragmentSignInBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseFragment
import com.jionex.agent.ui.base.BaseFragmentModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.di.DaggerSignInFragmentComponent
import com.jionex.agent.ui.main.di.SignInFragmentModule
import com.jionex.agent.ui.main.viewmodel.SignInViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Status
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDagger()
        initializeView(view)

    }

    private fun initializeDagger() {

        DaggerSignInFragmentComponent.builder().appComponent(JionexSDK.appComponent)
            .signInFragmentModule(SignInFragmentModule())
            .baseFragmentModule(BaseFragmentModule(mActivity)).build().inject(this)
    }

    private fun initializeView(view: View) {

        mDataBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString()
                        .isNotEmpty() && mDataBinding.etPassword.text!!.isNotEmpty() && mDataBinding.etPassword.text!!.length > 8
                ) {
                    mDataBinding.btnSignIn.background =
                        mActivity.getDrawable(R.drawable.btn_active_back)
                    mDataBinding.btnSignIn.isEnabled = true
                } else {
                    mDataBinding.btnSignIn.background =
                        mActivity.getDrawable(R.drawable.btn_inactive_back)
                    mDataBinding.btnSignIn.isEnabled = true
                }
            }
        })

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
                        mActivity.getDrawable(R.drawable.btn_active_back)
                    mDataBinding.btnSignIn.isEnabled = true
                } else {
                    mDataBinding.btnSignIn.background =
                        mActivity.getDrawable(R.drawable.btn_inactive_back)
                    mDataBinding.btnSignIn.isEnabled = true
                }
            }
        })

        mDataBinding.btnSignIn.setOnClickListener {
            name = mDataBinding.etName.text.toString()
            password = mDataBinding.etPassword.text.toString()
            if (name.isEmpty()) {
                callCustomToast(mActivity.getString(R.string.PLEASE_ENTER_NAME))
                //mDataBinding.etName.error = mActivity.getString(R.string.PLEASE_ENTER_NAME)
            } else if (password.isEmpty()) {
                callCustomToast(mActivity.getString(R.string.PLEASE_ENTER_PASSWORD))
                //mDataBinding.etPhoneNumber.error = mActivity.getString(R.string.PLEASE_ENTER_PHONE_NUMBER)
            } else {
                callSignInAPI(view)
            }
        }
        mDataBinding.etName.setText(viewmodel.getEmail().toString())
        mDataBinding.etPassword.setText(viewmodel.getPassword().toString())
        mDataBinding.etName.setText("agent1@gmail.com")
        mDataBinding.etPassword.setText("password")
    }

    private fun callSignInAPI(view: View) {

        if (networkHelper.isNetworkConnected()) {
            viewmodel.signInNow(
                SignInRequest(
                    email = name,
                    password = password
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
                        viewmodel.setEmail(it.data?.userDetail?.email.toString())
                        viewmodel.setFullName(it.data?.userDetail?.full_name.toString())
                        viewmodel.setParentId(it.data?.userDetail?.parent_id.toString())
                        viewmodel.setUserName(it.data?.userDetail?.user_name.toString())
                        viewmodel.setPhoneNumber(it.data?.userDetail?.phone.toString())
                        viewmodel.setUserId(it.data?.userDetail?.id.toString())
                        viewmodel.setUserRole(it.data?.userRoles?.get(0).toString())
                        Navigation.findNavController(view).navigate(
                            R.id.action_navigation_signin_to_navigation_verifyPin, bundle
                        )
                    }

                    Status.ERROR -> {
                        progressBar.dismiss()
                        val message = it.message.toString()
                        showMessage(message)
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

    private fun callCustomToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}