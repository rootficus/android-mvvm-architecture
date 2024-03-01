package com.rf.baseSideNav.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.rf.baseSideNav.R
import com.rf.baseSideNav.databinding.ActivitySplashBinding
import com.rf.baseSideNav.ui.base.BaseActivity
import com.rf.baseSideNav.ui.base.BaseViewModelFactory
import com.rf.baseSideNav.ui.main.viewmodel.SignInViewModel
import com.rf.baseSideNav.utils.NetworkHelper
import com.rf.baseSideNav.utils.SharedPreference
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