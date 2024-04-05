package com.rf.macgyver.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Keep
@Entity(tableName = "smsRecord")
data class SMSRecord(
    @PrimaryKey
    var messageId: String,
    var senderId: String? = null,
    var message: String? = null,
    var operator: String? = null,
    var receiver: String? = null,
    var deviceId: String? = null,
    var simSlot: String? = null,
    var date: String? = null,
    var smsType: Int = 0,
    var messageType: String? = null,
    var syncStatus: Int = 0
)