package com.rf.accessAli.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class CreateChallanResponse(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("challan_number") var challanNumber: String? = null,
    @SerializedName("valid_from") var validFrom: String? = null,
    @SerializedName("valid_to") var validTo: String? = null,
    @SerializedName("permit_start_date") var permitStartDate: String? = null,
    @SerializedName("permit_end_date") var permitEndDate: String? = null,
    @SerializedName("licence_type") var licenceType: String? = null,
    @SerializedName("name_style") var nameStyle: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("type_of_mineral") var typeOfMineral: String? = null,
    @SerializedName("quantity_granted") var quantityGranted: String? = null,
    @SerializedName("name_and_location") var nameAndLocation: String? = null,
    @SerializedName("types_of_product") var typesOfProduct: String? = null,
    @SerializedName("quantity_dispatched") var quantityDispatched: String? = null,
    @SerializedName("rate_of_mineral") var rateOfMineral: String? = null,
    @SerializedName("rate_of_mineral_total") var rateOfMineralTotal: String? = null,
    @SerializedName("gst_number") var gstNumber: String? = null,
    @SerializedName("quantity_percentage") var quantityPercentage: String? = null,
    @SerializedName("quantity_amount") var quantityAmount: String? = null,
    @SerializedName("vehicle_no") var vehicleNo: String? = null,
    @SerializedName("name_and_address") var nameAndAddress: String? = null,
    @SerializedName("driver_name") var driverName: String? = null,
    @SerializedName("driver_phone") var driverPhone: String? = null,
    @SerializedName("company_id") var companyId: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("route_source") var routeSource: String? = null,
    @SerializedName("route_designation") var routeDesignation: String? = null,
    @SerializedName("product") var product: String? = null

) : Serializable