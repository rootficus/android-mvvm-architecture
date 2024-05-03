package com.rf.accessAli.ui.base

import com.google.gson.annotations.Expose
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
    @Expose
    val data: ArrayList<P>? = null
) : ParentBaseModelResponse()

data class BaseResponseModel3<P>(
    @field:SerializedName("data")
    @Expose
    val data: List<P>? = null
) : ParentBaseModelResponse()

data class BaseResponseModelFilter<P>(
    @field:SerializedName("data")
    @Expose
    val data: ArrayList<P>? = null,
    @field:SerializedName("total_rows")
    @Expose
    val totalRows: Long? = null,
) : ParentBaseModelResponse()