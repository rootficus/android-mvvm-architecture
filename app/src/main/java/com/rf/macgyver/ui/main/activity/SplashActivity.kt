package com.rf.macgyver.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.rf.macgyver.R
import com.rf.macgyver.databinding.ActivitySplashBinding
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.sdkInit.UtellSDK
import com.rf.macgyver.ui.base.BaseActivity
import com.rf.macgyver.ui.base.BaseActivityModule
import com.rf.macgyver.ui.base.BaseViewModelFactory
import com.rf.macgyver.ui.main.viewmodel.SignInViewModel
import com.rf.macgyver.utils.NetworkHelper
import com.rf.macgyver.utils.SharedPreference
import com.rf.utellRestaurant.ui.main.di.DaggerSplashComponent
import com.rf.utellRestaurant.ui.main.di.SplashModule
import javax.inject.Inject


class SplashActivity : BaseActivity<ActivitySplashBinding, Any?>(R.layout.activity_splash) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


     @Inject
     lateinit var splashViewModelFactory: BaseViewModelFactory<SignInViewModel>
     private val viewmodel: SignInViewModel by viewModels { splashViewModelFactory }

    /*private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            viewDataBinding?.progressView?.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                    finishAffinity()
                }, 1000
            )
       }
        else {
            viewDataBinding?.progressView?.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
                    finishAffinity()
                }, 1000
            )
        }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this@SplashActivity)
        //FirebaseMessaging.getInstance().isAutoInitEnabled = true;
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

        val email = sharedPreference.getEmail()
        val password = sharedPreference.getPassword()
        val uniqueId = sharedPreference.getUniqueToken()

        val bundle = Bundle().apply {
            putString("uniqueId", uniqueId)
        }

        val loginDetails : LoginDetails? = viewmodel.getLoginDetails()
        if(email == null || password == null || uniqueId== null || email == ""  || password == "" || uniqueId == ""){
            viewDataBinding?.progressView?.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                    finishAffinity()
                }, 1000
            )
        }else{
            viewDataBinding?.progressView?.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    val intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                    intent.putExtra("bundle",bundle)
                    startActivity(intent)
                    finishAffinity()
                }, 1000
            )
        }

    }

    override fun onStop() {
        super.onStop()/*
        firebaseAuth.removeAuthStateListener(authStateListener)*/
    }
}