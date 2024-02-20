package com.rf.utellRestaurant.roomDB.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rf.utellRestaurant.roomDB.model.ModemSetting
import com.rf.utellRestaurant.roomDB.model.NotificationRecord
import com.rf.utellRestaurant.roomDB.model.OperatorRecord
import com.rf.utellRestaurant.roomDB.model.SMSRecord

@Dao
interface UtellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSMSRecord(smsRecord: SMSRecord)

    @Update
    fun updateSMSRecord(smsRecord: SMSRecord): Int

    @Transaction
    @Query("SELECT * FROM SMSRecord WHERE syncStatus != :syncStatus ")
    fun getSyncSMSRecord(syncStatus: Int): List<SMSRecord>

    @Query("SELECT  COUNT(messageId) FROM  SMSRecord")
    fun getSyncSMSRecordCount(): LiveData<Int>

    @Query("SELECT  COUNT(messageId) FROM  SMSRecord WHERE syncStatus != 1")
    fun getNotSyncSMSRecordCount(): LiveData<Int>

    @Query("SELECT  COUNT(messageId) FROM  SMSRecord WHERE syncStatus != 1")
    fun getNotSyncSMSRecordCountValue(): Int

    @Transaction
    @Query("Update smsRecord SET syncStatus =:syncStats Where messageId =:messageId")
    fun updateSyncResultRecord(syncStats: Int, messageId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModemSetting(modemSetting: ModemSetting)

    @Transaction
    @Query("SELECT * FROM modemSetting")
    fun getModemSetting(): List<ModemSetting>?

    @Transaction
    @Query("SELECT * FROM modemSetting where simId =:simId")
    fun getModemSetting(simId: Int): ModemSetting

    @Query("SELECT  COUNT(simId) FROM  modemSetting")
    fun getModemRegisterCountValue(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperatorRecord(operatorRecord: OperatorRecord)

    @Transaction
    @Query("SELECT * FROM operatorRecord where operatorName =:operatorName")
    fun getOperatorUSSD(operatorName: String): OperatorRecord


    @Query("SELECT EXISTS (SELECT * FROM notificationRecord WHERE id = :id)")
    fun isNotificationExits(id: String?): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notificationRecord: NotificationRecord)

}