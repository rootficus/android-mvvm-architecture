package com.jionex.agent.data.model.request

import com.google.gson.annotations.SerializedName

data class ModemJsonModel(
    @field:SerializedName("id")
    var id: Int = 0,
    @field:SerializedName("sim_type")
    var type: String? = null,
    @field:SerializedName("operator")
    var operator: String? = null,
    @field:SerializedName("phone_number")
    var phoneNumber: String? = null,
    @field:SerializedName("device_id")
    var deviceId: String? = null,
    @field:SerializedName("sim_id")
    var simId: String? = null,
    @field:SerializedName("token")
    var token: String? = null,
    @field:SerializedName("device_info")
    var deviceInfo: String? = null,
    @field:SerializedName("user_id")
    var userId: String? = null,
    @field:SerializedName("is_active")
    var is_active: Boolean = true,
    @field:SerializedName("pincode")
    var pincode: Int? = null


)
