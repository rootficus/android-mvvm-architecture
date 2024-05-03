package com.rf.accessAli.ui.main.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.rf.accessAli.ui.main.di.DaggerSignInComponent
import com.rf.accessAli.ui.main.di.SignInModule
import com.rf.accessAli.ui.main.viewmodel.SignInViewModel
import com.rf.accessAli.utils.NetworkHelper
import com.rf.accessAli.utils.SharedPreference
import com.rf.accessAli.R
import com.rf.accessAli.databinding.ActivitySignInBinding
import com.rf.accessAli.sdkInit.AccessAliSDK
import com.rf.accessAli.ui.base.BaseActivity
import com.rf.accessAli.ui.base.BaseActivityModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import javax.inject.Inject


class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by viewModels { signInViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initializationDagger()
    }

    private fun initializationDagger() {
        DaggerSignInComponent.builder().appComponent(AccessAliSDK.appComponent)
            .signInModule(SignInModule())
            .baseActivityModule(BaseActivityModule(this@SignInActivity)).build().inject(this)
    }

}