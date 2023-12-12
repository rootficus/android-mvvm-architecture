package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class MessagesJsonModel(
    @field:SerializedName("messages")
    val smsResultJsonModel: ArrayList<SMSResultJsonModel>
)
