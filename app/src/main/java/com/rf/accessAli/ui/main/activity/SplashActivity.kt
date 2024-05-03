package com.rf.accessAli.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.rf.accessAli.ui.base.BaseActivity
import com.rf.accessAli.ui.base.BaseActivityModule
import com.rf.accessAli.ui.base.BaseViewModelFactory
import com.rf.accessAli.ui.main.di.DaggerSplashComponent
import com.rf.accessAli.ui.main.di.SplashModule
import com.rf.accessAli.ui.main.viewmodel.SignInViewModel
import com.rf.accessAli.utils.NetworkHelper
import com.rf.accessAli.utils.SharedPreference
import com.rf.accessAli.R
import com.rf.accessAli.databinding.ActivitySplashBinding
import com.rf.accessAli.sdkInit.AccessAliSDK
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
        DaggerSplashComponent.builder().appComponent(AccessAliSDK.appComponent)
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
        //if (viewmodel.isLogin()) {
            viewDataBinding?.progressView?.visibility = View.GONE
            startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            finishAffinity()
        /*} else {
            viewDataBinding?.progressView?.visibility = View.GONE
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
            finishAffinity()
        }*/

    }

}