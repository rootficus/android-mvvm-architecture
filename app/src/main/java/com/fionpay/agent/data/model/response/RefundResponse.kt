package com.fionpay.agent.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class RefundResponse (
    @SerializedName("modem_id")
    @Expose
    var modemId: String?,

    @SerializedName("amount")
    @Expose
    var amount: Long?,

    @SerializedName("balance")
    @Expose
    var balance: String?,
)

