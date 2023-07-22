package com.jionex.agent.ui.main.activity

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import com.jionex.agent.R
import com.jionex.agent.databinding.ActivitySignInBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseActivity
import com.jionex.agent.ui.base.BaseActivityModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.di.DaggerSignInComponent
import com.jionex.agent.ui.main.di.SignInModule
import com.jionex.agent.viewmodel.SignInViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
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
        DaggerSignInComponent.builder().appComponent(JionexSDK.appComponent)
            .signInModule(SignInModule())
            .baseActivityModule(BaseActivityModule(this@SignInActivity)).build().inject(this)
    }




    override fun onResume() {
        super.onResume()
    }
}