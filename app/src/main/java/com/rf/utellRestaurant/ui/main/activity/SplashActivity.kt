package com.rf.utellRestaurant.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.rf.utellRestaurant.ui.base.BaseActivity
import com.rf.utellRestaurant.ui.base.BaseActivityModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import com.rf.utellRestaurant.ui.main.di.DaggerSplashComponent
import com.rf.utellRestaurant.ui.main.di.SplashModule
import com.rf.utellRestaurant.ui.main.viewmodel.SignInViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.ActivitySplashBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import javax.inject.Inject


class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var splashViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by viewModels { splashViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this@SplashActivity)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true;
        initializationDagger()
        initialization()
    }

    private fun initializationDagger() {
        DaggerSplashComponent.builder().appComponent(UtellSDK.appComponent)
            .splashModule(SplashModule())
            .baseActivityModule(BaseActivityModule(this@SplashActivity)).build()
            .inject(this)
    }

    private fun initialization() {

        viewDataBinding?.progressView?.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                goToNextScreen()
            }, 1000
        )
    }

    private fun goToNextScreen() {
        if (viewmodel.isLogin()) {
            startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            finishAffinity()
        } else {
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            finishAffinity()
        }

    }

}