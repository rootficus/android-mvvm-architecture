package com.jionex.agent.data.model.response

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
@Entity(tableName = "GetBalanceManageRecord")
data class GetBalanceManageRecord(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("sender")
    @Expose
    val sender: String? = null,

    @SerializedName("b_type")
    @Expose
    val bType: String? = null,

    @SerializedName("customer_account_no")
    @Expose
    val customerAccountNo: Long? = null,

    @SerializedName("agent_account_no")
    @Expose
    val agentAccountNo: Long? = null,

    @SerializedName("old_balance")
    @Expose
    val oldBalance: String? = null,

    @SerializedName("amount")
    @Expose val amount: String? = null,

    @SerializedName("commision")
    @Expose
    val commision: String? = null,

    @SerializedName("last_balance")
    @Expose
    val lastBalance: String? = null,

    @SerializedName("transaction_id")
    @Expose
    @PrimaryKey
    val transactionId: String,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("date")
    @Expose
    val date: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("user_id")
    @Expose
    val userId: String? = null,

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

