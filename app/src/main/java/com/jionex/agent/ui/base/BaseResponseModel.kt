package com.jionex.agent.ui.base

import com.google.gson.annotations.SerializedName

/**
 * Akash.Singh
 * RootFicus.
 */
data class BaseResponseModel<P>(
    @field:SerializedName("data")
    val data: P? = null
) : ParentBaseModelResponse()

data class BaseResponseModel2<P>(
    @field:SerializedName("data")
    val data: ArrayList<P>? = null
) : ParentBaseModelResponse()

data class BaseResponseModel3<P>(
    @field:SerializedName("data")
    val data: List<P>? = null
) : ParentBaseModelResponse()