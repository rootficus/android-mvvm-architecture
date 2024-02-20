package com.rf.utellRestaurant.ui.base

import com.rf.utellRestaurant.utils.SharedPreference
import javax.inject.Inject

/**
 * Akash.Singh
 * RootFicus.
 */
open class BaseSdk {
    @Inject
    lateinit var sharedPreference: SharedPreference
}