package com.rf.accessAli.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class CreateChallanRequest(

    @SerializedName("name_and_location") var nameAndLocation: String?= null,
    @SerializedName("product") var product: String?= null,
    @SerializedName("quantity_dispatched") var quantityDispatched: String?= null,
    @SerializedName("route_source") var routeSource: String?= null,
    @SerializedName("route_designation") var routeDesignation: String?= null,
    @SerializedName("vehicle_no") var vehicleNo: String?= null,
    @SerializedName("name_and_address") var nameAndAddress: String?= null,
    @SerializedName("driver_name") var driverName: String?= null,
    @SerializedName("driver_phone") var driverPhone: String?= null,
    @SerializedName("challan_number") var challanNumber: String?= null,
    @SerializedName("valid_from") var validFrom: String?= null,
    @SerializedName("valid_to") var validTo: String?= null,
    @SerializedName("gst_number") var gstNumber: String?= null,
    @SerializedName("expire_hour") var expireInHours: Int?= null,

    ) : Serializable