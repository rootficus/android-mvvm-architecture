package com.fionpay.agent.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class ReturnBalanceResponse(
    @SerializedName("balance")
    @Expose
    val balance: Float? = null,

    @SerializedName("hold_balance")
    @Expose
    val holdBalance: Float? = null,

    @SerializedName("available_balance")
    @Expose
    val availableBalance: Float? = null
    )

