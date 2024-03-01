package com.rf.baseSideNav.sdkInit

import android.app.Application
import com.rf.baseSideNav.ui.base.BaseSdk
import com.rf.baseSideNav.sdkInit.di.AppComponent
import com.rf.baseSideNav.sdkInit.di.AppModule
import com.rf.baseSideNav.sdkInit.di.DaggerAppComponent

object UtellSDK : BaseSdk() {
    private var mApplication: Application? = null
    lateinit var appComponent: AppComponent

    fun initialize(mApplication: Application): UtellSDK {
        UtellSDK.mApplication = mApplication
        appComponent = DaggerAppComponent.builder().appModule(AppModule(mApplication)).build()
        appComponent.inject(this)
        return this
    }

}