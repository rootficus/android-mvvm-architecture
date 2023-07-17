package com.jionex.agent.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.jionex.agent.R
import com.jionex.agent.databinding.ActivitySplashBinding
import com.jionex.agent.sdkInit.JionexSDK
import com.jionex.agent.ui.base.BaseActivity
import com.jionex.agent.ui.base.BaseActivityModule
import com.jionex.agent.ui.base.BaseViewModelFactory
import com.jionex.agent.ui.main.di.DaggerSplashComponent
import com.jionex.agent.ui.main.di.SplashModule
import com.jionex.agent.ui.main.fragment.DashBoardFragment
import com.jionex.agent.ui.main.viewmodel.SignInViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
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
        DaggerSplashComponent.builder().appComponent(JionexSDK.appComponent)
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
        if(viewmodel.isLogin()){
            startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            finishAffinity()
        }else{
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            finishAffinity()
        }

    }

}