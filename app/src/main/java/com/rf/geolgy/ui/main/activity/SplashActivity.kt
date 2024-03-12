package com.rf.geolgy.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.rf.geolgy.ui.base.BaseActivity
import com.rf.geolgy.ui.base.BaseActivityModule
import com.rf.geolgy.ui.base.BaseViewModelFactory
import com.rf.geolgy.ui.main.di.DaggerSplashComponent
import com.rf.geolgy.ui.main.di.SplashModule
import com.rf.geolgy.ui.main.viewmodel.SignInViewModel
import com.rf.geolgy.utils.NetworkHelper
import com.rf.geolgy.utils.SharedPreference
import com.rf.geolgy.R
import com.rf.geolgy.databinding.ActivitySplashBinding
import com.rf.geolgy.sdkInit.GeolgySDK
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initializationDagger()
        initialization()
    }

    private fun initializationDagger() {
        DaggerSplashComponent.builder().appComponent(GeolgySDK.appComponent)
            .splashModule(SplashModule())
            .baseActivityModule(BaseActivityModule(this@SplashActivity)).build()
            .inject(this)
    }

    private fun initialization() {
        viewDataBinding?.progressView?.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                goToNextScreen()
            }, 1000
        )
    }

    private fun goToNextScreen() {
        if (viewmodel.isLogin()) {
            viewDataBinding?.progressView?.visibility = View.GONE
            startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            finishAffinity()
        } else {
            viewDataBinding?.progressView?.visibility = View.GONE
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            finishAffinity()
        }

    }

}