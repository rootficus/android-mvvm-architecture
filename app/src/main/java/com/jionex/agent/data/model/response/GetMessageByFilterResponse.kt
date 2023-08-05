package com.jionex.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetMessageByFilterResponse(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("message_id")
    @Expose
    val messageId: String? = null,

    @SerializedName("text_message")
    @Expose
    val textMessage: String? = null,

    @SerializedName("sender")
    @Expose
    val sender: String? = null,

    @SerializedName("receiver")
    @Expose
    val receiver: String? = null,

    @SerializedName("sim_slot")
    @Expose
    val simSlot: String? = null,

    @SerializedName("sms_type")
    @Expose val smsType: String? = null,

    @SerializedName("android_id")
    @Expose
    val androidId: String? = null,

    @SerializedName("app_version")
    @Expose
    val appVersion: String? = null,

    @SerializedName("sms_date")
    @Expose
    val smsDate: String? = null,

    @SerializedName("is_active")
    @Expose
    val isActive: Any? = null,

    @SerializedName("user_id")
    @Expose
    val userId: String? = null,

    @SerializedName("transaction_type")
    @Expose
    val transactionType: String? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("formated_date")
    @Expose
    val formatedDate: String? = null,

    @SerializedName("formated_created_at")
    @Expose
    val formatedCreatedAt: String? = null,

    @SerializedName("date_in_words")
    @Expose
    val dateInWords: String? = null
): Serializable

