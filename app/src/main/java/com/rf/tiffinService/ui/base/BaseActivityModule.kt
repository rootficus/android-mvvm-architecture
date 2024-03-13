package com.rf.tiffinService.ui.base

import android.app.Dialog
import android.content.Context
import com.rf.tiffinService.utils.ActivityContext
import com.rf.tiffinService.utils.ActivityScope
import com.rf.tiffinService.utils.ApplicationContext
import com.rf.tiffinService.utils.Utility
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