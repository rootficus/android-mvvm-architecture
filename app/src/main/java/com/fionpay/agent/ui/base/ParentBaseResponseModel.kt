package com.fionpay.agent.ui.base

import com.google.gson.annotations.SerializedName

/**
 * Akash.Singh
 * RootFicus.
 */
open class ParentBaseResponseModel(
    @field:SerializedName("code")
    val code: Int?=null,
    @field:SerializedName("msg")
    val msg: String?=null,
    @field:SerializedName("message")
    val message: String?=null
)