package com.rf.macgyver.roomDB.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rf.macgyver.data.model.request.dailyReportData.QuestionData

class QuestionDataTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromQuestionData(questionData: QuestionData): String {
        return gson.toJson(questionData)
    }

    @TypeConverter
    fun toQuestionData(data: String): QuestionData {
        val type = object : TypeToken<QuestionData>() {}.type
        return gson.fromJson(data, type)
    }
}
