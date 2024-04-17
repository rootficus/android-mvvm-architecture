package com.rf.macgyver.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "incidentReport")
class IncidentReport(
    @PrimaryKey
    var incidentNo : String,
    var uniqueToken : String?,
    var incidentDate : String? = null,
    var incidentTime: String? = null,
    var incidentLocation: String? = null,
    var vehicleNo: String? = null,
    var vehicleName: String? = null,
    var operatorName : String? = null,
    var typeOfIncident : ArrayList<String> = arrayListOf(),
    var incidentArea : String? = null,
    var incidentSeverity : String? = null,
    var weatherCondition : String? = null,
    var vehicleActivity : String? = null,
    var incidentCause : String? = null,
    var damagesList : String? = null,
    var additionalComment :String? = null
)