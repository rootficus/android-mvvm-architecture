package com.rf.utellRestaurant.ui.main.activity

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import com.rf.utellRestaurant.ui.main.di.DaggerSignInComponent
import com.rf.utellRestaurant.ui.main.di.SignInModule
import com.rf.utellRestaurant.ui.main.viewmodel.SignInViewModel
import com.rf.utellRestaurant.utils.NetworkHelper
import com.rf.utellRestaurant.utils.SharedPreference
import com.rf.utellRestaurant.utils.Utility
import com.rf.utellRestaurant.R
import com.rf.utellRestaurant.databinding.ActivitySignInBinding
import com.rf.utellRestaurant.sdkInit.UtellSDK
import com.rf.utellRestaurant.ui.base.BaseActivity
import com.rf.utellRestaurant.ui.base.BaseActivityModule
import com.rf.utellRestaurant.ui.base.BaseViewModelFactory
import javax.inject.Inject


class SignInActivity : BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var signInViewModelFactory: BaseViewModelFactory<SignInViewModel>
    private val viewmodel: SignInViewModel by viewModels { signInViewModelFactory }
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializationDagger()
    }

    private fun initializationDagger() {
        DaggerSignInComponent.builder().appComponent(UtellSDK.appComponent)
            .signInModule(SignInModule())
            .baseActivityModule(BaseActivityModule(this@SignInActivity)).build().inject(this)
    }


    override fun onResume() {
        super.onResume()
        //Utility.getPushToken(this@SignInActivity)
    }
}