package com.fionpay.agent.data.model.request

import com.google.gson.annotations.SerializedName
import com.fionpay.agent.data.model.request.SMSResultJsonModel

data class MessagesJsonModel(
    @field:SerializedName("messages")
    val smsResultJsonModel: ArrayList<SMSResultJsonModel>)
