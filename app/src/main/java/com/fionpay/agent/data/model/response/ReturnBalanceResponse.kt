package com.fionpay.agent.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class ReturnBalanceResponse(
    @SerializedName("current_balance")
    @Expose
    var currentBalance: Double?,

    )

