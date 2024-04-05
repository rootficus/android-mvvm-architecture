package com.rf.macgyver.ui.main.activity

import android.os.Bundle
import com.rf.macgyver.R
import com.rf.macgyver.databinding.ActivitySignInBinding
import com.rf.macgyver.ui.base.BaseActivity


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



}