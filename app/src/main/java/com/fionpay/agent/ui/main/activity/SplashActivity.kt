package com.fionpay.agent.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.fionpay.agent.R
import com.fionpay.agent.databinding.ActivitySplashBinding
import com.fionpay.agent.sdkInit.FionSDK
import com.fionpay.agent.ui.base.BaseActivity
import com.fionpay.agent.ui.base.BaseActivityModule
import com.fionpay.agent.ui.base.BaseViewModelFactory
import com.fionpay.agent.ui.main.di.DaggerSplashComponent
import com.fionpay.agent.ui.main.di.SplashModule
import com.fionpay.agent.ui.main.viewmodel.SignInViewModel
import com.fionpay.agent.utils.NetworkHelper
import com.fionpay.agent.utils.SharedPreference
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

        initializationDagger()
        initialization()
    }

    private fun initializationDagger() {
        DaggerSplashComponent.builder().appComponent(FionSDK.appComponent)
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