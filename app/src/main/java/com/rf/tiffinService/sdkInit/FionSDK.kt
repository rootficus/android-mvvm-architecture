package com.rf.tiffinService.sdkInit

import android.app.Application
import com.rf.tiffinService.ui.base.BaseSdk
import com.rf.tiffinService.sdkInit.di.AppComponent
import com.rf.tiffinService.sdkInit.di.AppModule
import com.rf.tiffinService.sdkInit.di.DaggerAppComponent

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