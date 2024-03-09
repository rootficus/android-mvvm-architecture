package com.rf.geolgy.roomDB.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rf.geolgy.roomDB.model.ModemSetting
import com.rf.geolgy.roomDB.model.NotificationRecord
import com.rf.geolgy.roomDB.model.OperatorRecord
import com.rf.geolgy.roomDB.model.SMSRecord

@Dao
interface GeolgyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSMSRecord(smsRecord: SMSRecord)

    @Update
    fun updateSMSRecord(smsRecord: SMSRecord): Int

    @Transaction
    @Query("SELECT * FROM SMSRecord WHERE syncStatus != :syncStatus ")
    fun getSyncSMSRecord(syncStatus: Int): List<SMSRecord>

}