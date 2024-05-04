package com.rf.accessAli.data.repository

import android.content.Context
import com.rf.accessAli.data.remote.AccessAliApiServices
import com.rf.accessAli.roomDB.AccessAliDatabase
import com.rf.accessAli.roomDB.model.UniversityData
import com.rf.accessAli.ui.base.BaseRepository
import com.rf.accessAli.utils.SharedPreference

class DashBoardRepository(
    val apiServices: AccessAliApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val accessAliDatabase: AccessAliDatabase
) : BaseRepository() {

    fun getUniversityData(
        success: (universityData: List<UniversityData>) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getUniversityData().apply {
            execute(this, context, success, fail)
        }
    }

    fun insertUniversityData(universityData: List<UniversityData>)
    {
        accessAliDatabase.accessAliDao()?.insertUniversityData(universityData)
    }

    fun getUniversityData() : List<UniversityData>?
    {
        return accessAliDatabase.accessAliDao()?.getUniversityData()

    }

}