package com.rf.macgyver.roomDB.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rf.macgyver.data.model.request.inspectionFormData.IPQuestionData

class IPQuestionTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromIPQuestionData(ipQuestionData: IPQuestionData): String {
        return gson.toJson(ipQuestionData)
    }

    @TypeConverter
    fun toQuestionData(data: String): IPQuestionData {
        val type = object : TypeToken<IPQuestionData>() {}.type
        return gson.fromJson(data, type)
    }
}