package com.fionpay.agent.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class UpdateBalanceRequest(
    @field:SerializedName("balance_status")
    var balance_status: String,
    @field:SerializedName("id")
    var id: String,
    @field:SerializedName("user_id")
    var user_id:String
)
