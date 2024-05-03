package com.rf.accessAli.roomDB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rf.accessAli.roomDB.model.UniversityData

@Dao
interface AccessAliDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUniversityData(universityData: List<UniversityData>)


    @Transaction
    @Query("SELECT * FROM UniversityData ")
    fun getUniversityData(): List<UniversityData>
}