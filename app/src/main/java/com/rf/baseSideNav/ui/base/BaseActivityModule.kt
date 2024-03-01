package com.rf.baseSideNav.ui.base

import android.app.Dialog
import android.content.Context
import com.rf.baseSideNav.utils.ActivityContext
import com.rf.baseSideNav.utils.ActivityScope
import com.rf.baseSideNav.utils.ApplicationContext
import com.rf.baseSideNav.utils.Utility
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