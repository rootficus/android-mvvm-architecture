package com.jionex.agent.ui.base

import android.app.Dialog
import android.content.Context
import com.jionex.agent.utils.ActivityContext
import com.jionex.agent.utils.ActivityScope
import com.jionex.agent.utils.ApplicationContext
import com.jionex.agent.utils.Utility
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