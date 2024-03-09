package com.rf.geolgy.ui.base

import com.rf.geolgy.utils.SharedPreference
import javax.inject.Inject

/**
 * Akash.Singh
 * RootFicus.
 */
open class BaseSdk {
    @Inject
    lateinit var sharedPreference: SharedPreference
}