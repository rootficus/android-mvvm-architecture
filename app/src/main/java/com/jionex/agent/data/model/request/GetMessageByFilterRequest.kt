package com.jionex.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class GetMessageByFilterRequest(
    @field:SerializedName("page_size")
    var page_size: Int = 0,
    @field:SerializedName("page_number")
    var page_number: Int? = null,
/*    @field:SerializedName("sender")
    var sender: String? = null,
    @field:SerializedName("agent_account_no")
    var agent_account_no: Int? = null,
    @field:SerializedName("transaction_id")
    var transaction_id: String? = null,
    @field:SerializedName("type")
    var type: String? = null,
    @field:SerializedName("from")
    var from: String? = null,
    @field:SerializedName("to")
    var to: String? = null,*/
    @field:SerializedName("message_type")
    var message_type: Int? = -1,
)