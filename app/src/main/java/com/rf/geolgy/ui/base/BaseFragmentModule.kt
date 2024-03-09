package com.rf.geolgy.ui.base

import android.app.Dialog
import android.content.Context
import com.rf.geolgy.utils.FragmentScope
import com.rf.geolgy.utils.Utility
import dagger.Module
import dagger.Provides

/**
 * Akash.Singh
 * RootFicus.
 */
@Module
class BaseFragmentModule(private val context: Context) {
    /**
     * common progressbar dependency
     */
    @Provides
    @FragmentScope
    fun getProgressBar(): Dialog = Utility.showCommonProgressDialog(context)

}