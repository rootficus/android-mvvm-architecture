package com.rf.geolgy.roomDB.model

import com.google.gson.annotations.SerializedName

data class NotificationMessageBody(
    @SerializedName("id")
    var id: String,
    @SerializedName("message_body")
    var messageBody: String? = null,
    @SerializedName("date")
    var date: String? = null
)