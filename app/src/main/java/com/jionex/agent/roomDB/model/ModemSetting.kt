package com.jionex.agent.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "modemSetting")
data class ModemSetting(
    @PrimaryKey
    var simId: Int,
    var type:String? =null,
    var operator: String? = null,
    var phoneNumber: String? = null,
    var deviceId: String? = null,
    var token: String? = null,
    var deviceInfo: String? = null,
    var userId: String? = null,
    var pinCode:Int?=null,
    var isActive: Boolean = true)
