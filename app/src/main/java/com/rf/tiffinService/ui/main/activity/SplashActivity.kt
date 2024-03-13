package com.rf.tiffinService.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.rf.tiffinService.R
import com.rf.tiffinService.databinding.ActivitySplashBinding
import com.rf.tiffinService.ui.base.BaseActivity
import com.rf.tiffinService.utils.NetworkHelper
import com.rf.tiffinService.utils.SharedPreference
import javax.inject.Inject


class SplashActivity : BaseActivity<ActivitySplashBinding, Any?>(R.layout.activity_splash) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

   /* @Inject
    lateinit var splashViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by viewModels { splashViewModelFactory }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this@SplashActivity)
        //FirebaseMessaging.getInstance().isAutoInitEnabled = true;
        initializationDagger()
        initialization()
    }

    private fun initializationDagger() {
       /* DaggerSplashComponent.builder().appComponent(UtellSDK.appComponent)
            .splashModule(SplashModule())
            .baseActivityModule(BaseActivityModule(this@SplashActivity)).build()
            .inject(this)*/
    }

    private fun initialization() {

        viewDataBinding?.progressView?.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                finishAffinity()
                goToNextScreen()
            }, 1000
        )
    }

    private fun goToNextScreen() {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            finishAffinity()

    }
}