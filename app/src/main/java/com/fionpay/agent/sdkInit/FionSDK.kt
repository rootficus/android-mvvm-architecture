package com.fionpay.agent.sdkInit

import android.app.Application
import com.fionpay.agent.sdkInit.di.AppComponent
import com.fionpay.agent.sdkInit.di.AppModule
import com.fionpay.agent.sdkInit.di.DaggerAppComponent
import com.fionpay.agent.ui.base.BaseSdk

object FionSDK : BaseSdk() {
    private var mApplication: Application? = null
    lateinit var appComponent: AppComponent

    fun initialize(mApplication: Application): FionSDK {
        this.mApplication = mApplication
        appComponent = DaggerAppComponent.builder().appModule(AppModule(mApplication)).build()
        appComponent.inject(this)
        return this
    }

}