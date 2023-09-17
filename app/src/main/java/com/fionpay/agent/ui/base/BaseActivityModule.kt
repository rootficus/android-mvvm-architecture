package com.fionpay.agent.ui.base

import android.app.Dialog
import android.content.Context
import com.fionpay.agent.utils.ActivityContext
import com.fionpay.agent.utils.ActivityScope
import com.fionpay.agent.utils.ApplicationContext
import com.fionpay.agent.utils.Utility
import dagger.Module
import dagger.Provides

/**
 * Akash.Singh
 * RootFicus.
 */
@Module
open class BaseActivityModule(private val context: Context) {

        /**
         * common progressbar dependency
         */
        @Provides
        @ActivityScope
        fun getProgressBar(@ApplicationContext context: Context):Dialog= Utility.showCommonProgressDialog(context)

       @ActivityContext
       @Provides
       @ActivityScope
       fun getActivityContext()=context
}