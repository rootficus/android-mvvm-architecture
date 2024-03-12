package com.rf.geolgy.ui.main.activity

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.rf.geolgy.ui.main.di.DaggerSignInComponent
import com.rf.geolgy.ui.main.di.SignInModule
import com.rf.geolgy.ui.main.viewmodel.SignInViewModel
import com.rf.geolgy.utils.NetworkHelper
import com.rf.geolgy.utils.SharedPreference
import com.rf.geolgy.R
import com.rf.geolgy.databinding.ActivitySignInBinding
import com.rf.geolgy.sdkInit.GeolgySDK
import com.rf.geolgy.ui.base.BaseActivity
import com.rf.geolgy.ui.base.BaseActivityModule
import com.rf.geolgy.ui.base.BaseViewModelFactory
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
        DaggerSignInComponent.builder().appComponent(GeolgySDK.appComponent)
            .signInModule(SignInModule())
            .baseActivityModule(BaseActivityModule(this@SignInActivity)).build().inject(this)
    }

}