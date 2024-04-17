package com.rf.macgyver.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rf.macgyver.data.model.request.dailyReportData.QuestionData
import com.rf.macgyver.data.model.request.inspectionFormData.IPQuestionData

@Keep
@Entity(tableName = "inspectionForm")
data class InspectionForm(
    @PrimaryKey
    var uniqueId: Int,
    var uniqueToken : String? = null,
    var heading : String? = null,
    var faultyItems :String? = null,
    var overallCond: String? = null,
    var vehicleStatus: String? = null,
    var priority: String? = null,
    var isSafeToUse: String? = null,
    var isDeployed: String? = null,
    var additionalNote: String? =null,
    var question1: IPQuestionData? =null,
    var question2: IPQuestionData? =null,
    var question3: IPQuestionData? =null,
    var question4: IPQuestionData? =null,
    var question5: IPQuestionData? =null,
    var question6: IPQuestionData? =null,
    var question7: IPQuestionData? =null,
    var question8: IPQuestionData? =null,
    var question9: IPQuestionData? =null,
    var question10: IPQuestionData? =null
)
