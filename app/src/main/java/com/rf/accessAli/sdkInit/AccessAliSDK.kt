package com.rf.accessAli.sdkInit

import android.app.Application
import com.rf.accessAli.ui.base.BaseSdk
import com.rf.accessAli.sdkInit.di.AppComponent
import com.rf.accessAli.sdkInit.di.AppModule
import com.rf.accessAli.sdkInit.di.DaggerAppComponent

object AccessAliSDK : BaseSdk() {
    private var mApplication: Application? = null
    lateinit var appComponent: AppComponent

    fun initialize(mApplication: Application): AccessAliSDK {
        AccessAliSDK.mApplication = mApplication
        appComponent = DaggerAppComponent.builder().appModule(AppModule(mApplication)).build()
        appComponent.inject(this)
        return this
    }

}