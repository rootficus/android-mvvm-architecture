package com.fionpay.agent.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Keep
@Entity(tableName = "operatorRecord")
data class OperatorRecord(
    @PrimaryKey
    var operatorName: String,
    var operatorPhoneNumberUSSD: String? = null
)