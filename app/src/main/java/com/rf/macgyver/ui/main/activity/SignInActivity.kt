package com.rf.macgyver.ui.main.activity

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import com.rf.macgyver.R
import com.rf.macgyver.databinding.ActivitySignInBinding
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseActivity
import com.rf.macgyver.ui.base.BaseActivityModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.di.DaggerSignInComponent
import com.rf.macgyver.ui.main.di.SignInModule
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.utils.NetworkHelper
import javax.inject.Inject


class SignInActivity : BaseActivity<ActivitySignInBinding, Any?>(R.layout.activity_sign_in) {
    @Inject
    lateinit var networkHelper: NetworkHelper


    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by viewModels { signInViewModelFactory }
    var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializationDagger()
        initialization()
    }

    private fun initialization() {

    }


    private fun initializationDagger() {
        DaggerSignInComponent.builder().appComponent(UtellSDK.appComponent)
            .signInModule(SignInModule())
            .baseActivityModule(BaseActivityModule(this@SignInActivity)).build()
            .inject(this)
    }


    override fun onResume() {
        super.onResume()

    }


}