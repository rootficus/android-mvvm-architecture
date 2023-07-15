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
import com.jionex.agent.ui.main.viewmodel.AgentVerificationViewModel
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import javax.inject.Inject


class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var splashViewModelFactory: BaseViewModelFactory<AgentVerificationViewModel>
    private val viewmodel: AgentVerificationViewModel by viewModels { splashViewModelFactory }
    var bundle: Bundle? = null
    var type: String? = ""
    var moduleTab: String? = ""
    var userName: String? = ""
    var userMessage: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializationDagger()
        initialization()
        checkIntentData()
    }

    private fun checkIntentData() {
        try {
            bundle = intent.extras
            type = bundle?.getString("type")
            moduleTab = bundle?.getString("moduleName")
            userName = bundle?.getString("name")
            userMessage = bundle?.getString("message")
            Log.i("BundleSplash::", ":::${bundle.toString()}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initializationDagger() {
        DaggerSplashComponent.builder().appComponent(JionexSDK.appComponent)
            .splashModule(SplashModule())
            .baseActivityModule(BaseActivityModule(this@SplashActivity)).build()
            .inject(this)
    }

    private fun initialization() {

        viewDataBinding?.progressView?.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(applicationContext, SignInActivity::class.java))
                finish()
            }, 3000
        )
    }

}