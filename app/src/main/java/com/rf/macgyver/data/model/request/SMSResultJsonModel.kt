package com.rf.macgyver.data.model.request

import com.google.gson.annotations.SerializedName

data class SMSResultJsonModel(
    @field:SerializedName("id")
    var id: Int = 0,
    @field:SerializedName("message_id")
    var messageId: String? = null,
    @field:SerializedName("text_message")
    var textMessage: String? = null,
    @field:SerializedName("sender")
    var sender: String? = null,
    @field:SerializedName("receiver")
    var receiver: String? = null,
    @field:SerializedName("sim_slot")
    var simSlot: String? = null,
    @field:SerializedName("sms_type")
    var smsType: Int? = null,
    @field:SerializedName("transaction_type")
    var transactionType: String? = null,
    @field:SerializedName("android_id")
    var androidId: String? = null,
    @field:SerializedName("app_version")
    var appVersion: String? = null,
    @field:SerializedName("sms_date")
    var smsDate: String? = null,
    @field:SerializedName("user_id")
    var userId: String? = "e636e301-d772-42b7-81f5-b9ebdc12832f",
)
