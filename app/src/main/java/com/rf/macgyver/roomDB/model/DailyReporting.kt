package com.rf.macgyver.roomDB.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rf.macgyver.data.model.request.dailyReportData.QuestionData

@Keep
@Entity(tableName = "dailyReporting")
data class DailyReporting(
    @PrimaryKey
    var reportName: String,
    var uniqueToken : String?,
    var name : String? = null,
    var shift :String? = null,
    var vehicleNo: String? = null,
    var vehicleName: String? = null,
    var date: String? = null,
    var day: String? = null,
    var question1: QuestionData? =null,
    var question2: QuestionData? =null,
    var question3: QuestionData? =null,
    var question4: QuestionData? =null,
    var question5: QuestionData? =null,
    var question6: QuestionData? =null,
    var question7: QuestionData? =null,
    var question8: QuestionData? =null,
    var question9: QuestionData? =null,
    var vehicleDowntime: String? = null,
    var downtimeNote: String? =null,
    var vehicleRuntime: String? = null,
    var vehicleWorkLog: String? = null
)
