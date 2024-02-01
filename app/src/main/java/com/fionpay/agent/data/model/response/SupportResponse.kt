package com.fionpay.agent.data.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SupportResponse(
    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("ticket_number")
    var ticketNumber: String? = null,

    @field:SerializedName("subject")
    var subject: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("status")
    var status: String? = null,

    @field:SerializedName("user_id")
    var userId: String? = null,

    @field:SerializedName("team_id")
    var teamId: String? = null,

    @field:SerializedName("created_at")
    var createdAt: String? = null,

    @field:SerializedName("updated_at")
    var updatedAt: String? = null,

    @field:SerializedName("seen")
    var seen: String? = null,
) : Serializable
