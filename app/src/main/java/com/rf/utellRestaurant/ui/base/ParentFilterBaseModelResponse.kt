package com.rf.utellRestaurant.ui.base

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
open class ParentFilterBaseModelResponse(
    @field:SerializedName("message")
    @Expose
    val message: String? = null,

    @field:SerializedName("status")
    @Expose
    val status: Boolean? = null,

    @field:SerializedName("error")
    @Expose
    val error: String? = null

)