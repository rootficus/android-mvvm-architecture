package com.jionex.agent.sdkInit

import android.app.Application
import com.jionex.agent.sdkInit.di.AppComponent
import com.jionex.agent.sdkInit.di.AppModule
import com.jionex.agent.ui.base.BaseSdk
import com.jionex.agent.sdkInit.di.DaggerAppComponent

object JionexSDK : BaseSdk() {
    private var mApplication: Application? = null
    lateinit var appComponent: AppComponent

    fun initialize(mApplication: Application): JionexSDK {
        this.mApplication = mApplication
        appComponent = DaggerAppComponent.builder().appModule(AppModule(mApplication)).build()
        appComponent.inject(this)
        return this
    }

}