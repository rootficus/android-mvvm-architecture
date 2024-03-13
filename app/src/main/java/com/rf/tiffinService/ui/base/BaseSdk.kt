package com.rf.tiffinService.ui.base

import com.rf.tiffinService.utils.SharedPreference
import javax.inject.Inject

/**
 * Akash.Singh
 * RootFicus.
 */
open class BaseSdk {
    @Inject
    lateinit var sharedPreference: SharedPreference
}