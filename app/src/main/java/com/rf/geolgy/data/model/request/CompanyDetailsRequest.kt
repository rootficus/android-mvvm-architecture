package com.rf.geolgy.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class CompanyDetailsRequest(

    @SerializedName("pincode") var pincode: String? = null,

    ) : Serializable