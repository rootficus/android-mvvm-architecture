package com.rf.baseSideNav.ui.main.activity

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import com.rf.baseSideNav.ui.main.viewmodel.SignInViewModel
import com.rf.baseSideNav.utils.NetworkHelper
import com.rf.baseSideNav.R
import com.rf.baseSideNav.databinding.ActivitySignInBinding
import com.rf.baseSideNav.databinding.FragmentOtpBinding
import com.rf.baseSideNav.databinding.FragmentSignInBinding
import com.rf.baseSideNav.sdkInit.UtellSDK
import com.rf.baseSideNav.ui.base.BaseActivity
import com.rf.baseSideNav.ui.base.BaseActivityModule
import com.rf.baseSideNav.ui.base.BaseViewModelFactory
import javax.inject.Inject


class SignInActivity : BaseActivity<ActivitySignInBinding, Any?>(R.layout.activity_sign_in) {
/*
    @Inject
    lateinit var networkHelper: NetworkHelper


    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by viewModels { signInViewModelFactory }
    var dialog: Dialog? = null
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val SignInView = FragmentSignInBinding.inflate(layoutInflater)


    }
/*
    private fun initializationDagger() {*//*
        DaggerSignInComponent.builder().appComponent(UtellSDK.appComponent)
            .signInModule(SignInModule())
            .baseActivityModule(BaseActivityModule(this@SignInActivity)).build().inject(this)*//*
    }

*/
    override fun onResume() {
        super.onResume()
        //Utility.getPushToken(this@SignInActivity)
    }

    private fun selectContinue() {

        val view = FragmentOtpBinding.inflate(layoutInflater)

    }
}