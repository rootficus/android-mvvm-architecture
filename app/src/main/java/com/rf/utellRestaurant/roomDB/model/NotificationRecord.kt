package com.rf.utellRestaurant.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rf.utellRestaurant.utils.Utility

@Keep
@Entity(tableName = "notificationRecord")
class NotificationRecord(
    @PrimaryKey
    var id: String,
    var notificationType: String? = null,
    var messageBody: String? = null,
    var isRead: Boolean = false,
    var isSend: Boolean = false,
    var createdAt: String? = Utility.getCurrentDateTimeFormat()
)