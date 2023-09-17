package com.fionpay.agent.ui.base

import com.fionpay.agent.utils.SharedPreference
import javax.inject.Inject

/**
 * Akash.Singh
 * RootFicus.
 */
open class BaseSdk {
    @Inject
    lateinit var sharedPreference: SharedPreference
}