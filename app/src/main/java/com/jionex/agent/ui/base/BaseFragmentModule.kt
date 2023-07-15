package com.jionex.agent.ui.base

import android.app.Dialog
import android.content.Context
import com.jionex.agent.utils.FragmentScope
import com.jionex.agent.utils.Utility
import dagger.Module
import dagger.Provides

/**
 * Akash.Singh
 * RootFicus.
 */
@Module
 class BaseFragmentModule(private val context: Context){
    /**
     * common progressbar dependency
     */
    @Provides
    @FragmentScope
    fun getProgressBar(): Dialog = Utility.showCommonProgressDialog(context)

}