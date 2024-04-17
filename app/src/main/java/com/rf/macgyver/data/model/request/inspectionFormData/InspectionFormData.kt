package com.rf.macgyver.data.model.request.inspectionFormData

import java.io.Serializable


data class InspectionFormData(
    var title : String? = null,
    var concernLevel : String? = null,
    var circle1 : Boolean? = false,
    var circle2 : Boolean? = false,
    var circle3 : Boolean? = false,
    var circle4 : Boolean? = false,
    var circle5 : Boolean? = false,
): Serializable