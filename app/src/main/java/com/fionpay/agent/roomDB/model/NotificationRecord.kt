package com.fionpay.agent.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fionpay.agent.utils.Utility

@Keep
@Entity(tableName = "notificationRecord")
class NotificationRecord (
    @PrimaryKey
    var id : String,
    var notificationType: String? = null,
    var messageBody: String? = null,
    var isRead:Boolean = false,
    var isSend:Boolean = false,
    var createdAt: String? = Utility.getCurrentDateTimeFormat())