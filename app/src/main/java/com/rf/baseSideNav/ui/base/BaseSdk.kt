package com.rf.baseSideNav.ui.base

import com.rf.baseSideNav.utils.SharedPreference
import javax.inject.Inject

/**
 * Akash.Singh
 * RootFicus.
 */
open class BaseSdk {
    @Inject
    lateinit var sharedPreference: SharedPreference
}