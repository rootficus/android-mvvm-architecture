package com.rf.tiffinService.ui.main.activity

import android.content.Intent
import android.os.Bundle
import com.rf.tiffinService.R
import com.rf.tiffinService.databinding.ActivityDashboardBinding
import com.rf.tiffinService.databinding.ActivitySignInBinding
import com.rf.tiffinService.databinding.FragmentOtpBinding
import com.rf.tiffinService.databinding.FragmentSignInBinding
import com.rf.tiffinService.ui.base.BaseActivity


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

        val SignInView = FragmentOtpBinding.inflate(layoutInflater)

        /*SignInView.btnValidate.setOnClickListener {
            startActivity(Intent(this@SignInActivity, DashBoardActivity::class.java))
            finishAffinity() }*/

    }/*
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

    private fun selectValidate() {

        val view = ActivityDashboardBinding.inflate(layoutInflater)

    }

}