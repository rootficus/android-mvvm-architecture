package com.fionpay.agent.data.model.request

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class FilterResponse(
    @field:SerializedName("banks")
    var banks: List<Bank>? = null,
    @field:SerializedName("modems")
    var modems: List<Modem>? = null
) : Serializable

@Keep
@Entity(tableName = "banks")
data class Bank(
    @PrimaryKey
    @ColumnInfo(name = "bank_id")
    @SerializedName("bank_id")
    val bankId: Int? = null,

    @ColumnInfo(name = "bank_name")
    @SerializedName("bank_name")
    val bankName: String? = null,

    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    var phoneNumber: String? = null,

    @ColumnInfo(name = "bank_image")
    @SerializedName("bank_image")
    var bankImage: String? = null
)

@Keep
@Entity(tableName = "modems")
data class Modem(
    @PrimaryKey
    @ColumnInfo(name = "modem_slot_id")
    @SerializedName("modem_slot_id")
    val modemSlotId: Int? = null,

    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    val phoneNumber: String? = null
)