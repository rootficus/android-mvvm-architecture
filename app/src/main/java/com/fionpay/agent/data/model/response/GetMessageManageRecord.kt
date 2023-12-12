package com.fionpay.agent.data.model.response

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(tableName = "GetMessageManageRecord")
data class GetMessageManageRecord(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("message_id")
    @Expose
    @PrimaryKey
    val messageId: String,

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
    @Expose val smsType: Int = 0,

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
    val isActive: Boolean? = null,

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
) : Serializable

