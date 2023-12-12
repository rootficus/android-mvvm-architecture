package com.fionpay.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetStatusCountResponse(
    @SerializedName("success")
    @Expose
    val success: Int? = null,

    @SerializedName("rejected")
    @Expose
    val rejected: Int? = null,

    @SerializedName("approved")
    @Expose
    val approved: Int? = null,

    @SerializedName("pending")
    @Expose
    val pending: Int? = null,

    @SerializedName("danger")
    @Expose
    val danger: Int? = null,

    ) : Serializable

