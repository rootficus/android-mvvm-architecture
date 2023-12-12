package com.fionpay.agent.ui.main.activity

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import com.fionpay.agent.R
import com.fionpay.agent.databinding.ActivitySignInBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseActivity
import com.fionpay.agent.ui.base.BaseActivityModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.di.DaggerSignInComponent
import com.fionpay.agent.ui.main.di.SignInModule
import com.fionpay.agent.ui.main.viewmodel.SignInViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
import com.fionpay.agent.utils.Utility
import javax.inject.Inject


class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by viewModels { signInViewModelFactory }
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializationDagger()
    }

    private fun initializationDagger() {
        DaggerSignInComponent.builder().appComponent(FionSDK.appComponent)
            .signInModule(SignInModule())
            .baseActivityModule(BaseActivityModule(this@SignInActivity)).build().inject(this)
    }


    override fun onResume() {
        super.onResume()
        Utility.getPushToken(this@SignInActivity)
    }
}