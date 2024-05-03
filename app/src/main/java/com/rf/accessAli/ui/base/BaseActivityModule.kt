package com.rf.accessAli.ui.base

import android.app.Dialog
import android.content.Context
import com.rf.accessAli.utils.ActivityContext
import com.rf.accessAli.utils.ActivityScope
import com.rf.accessAli.utils.ApplicationContext
import com.rf.accessAli.utils.Utility
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
    fun getProgressBar(@ApplicationContext context: Context): Dialog =
        Utility.showCommonProgressDialog(context)

    @ActivityContext
    @Provides
    @ActivityScope
    fun getActivityContext() = context
}