package com.jionex.agent.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetBalanceByFilterResponse(
    @SerializedName("id")
    @Expose
    private val id: String? = null,

    @SerializedName("sender")
    @Expose
    private val sender: String? = null,

    @SerializedName("b_type")
    @Expose
    private val bType: String? = null,

    @SerializedName("customer_account_no")
    @Expose
    private val customerAccountNo: Int? = null,

    @SerializedName("agent_account_no")
    @Expose
    private val agentAccountNo: Int? = null,

    @SerializedName("old_balance")
    @Expose
    private val oldBalance: String? = null,

    @SerializedName("amount")
    @Expose
    private val amount: String? = null,

    @SerializedName("commision")
    @Expose
    private val commision: String? = null,

    @SerializedName("last_balance")
    @Expose
    private val lastBalance: String? = null,

    @SerializedName("transaction_id")
    @Expose
    private val transactionId: String? = null,

    @SerializedName("status")
    @Expose
    private val status: String? = null,

    @SerializedName("date")
    @Expose
    private val date: String? = null,

    @SerializedName("message")
    @Expose
    private val message: String? = null,

    @SerializedName("user_id")
    @Expose
    private val userId: String? = null,

    @SerializedName("created_at")
    @Expose
    private val createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    private val updatedAt: String? = null,

    @SerializedName("formated_date")
    @Expose
    private val formatedDate: String? = null,

    @SerializedName("formated_created_at")
    @Expose
    private val formatedCreatedAt: String? = null,

    @SerializedName("date_in_words")
    @Expose
    private val dateInWords: String? = null
): Serializable

