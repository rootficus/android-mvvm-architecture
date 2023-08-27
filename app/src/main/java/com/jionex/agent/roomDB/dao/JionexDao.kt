package com.jionex.agent.roomDB.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jionex.agent.data.model.response.GetBalanceManageRecord
import com.jionex.agent.data.model.response.GetMessageManageRecord
import com.jionex.agent.roomDB.model.ModemSetting
import com.jionex.agent.roomDB.model.OperatorRecord
import com.jionex.agent.roomDB.model.SMSRecord

@Dao
interface JionexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSMSRecord(smsRecord: SMSRecord?)

    @Update
    fun updateSMSRecord(smsRecord: SMSRecord?): Int

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
    fun insertModemSetting(modemSetting: ModemSetting?)

    @Transaction
    @Query("SELECT * FROM modemSetting")
    fun getModemSetting(): List<ModemSetting?>?

    @Transaction
    @Query("SELECT * FROM modemSetting where simId =:simId")
    fun getModemSetting(simId: Int): ModemSetting?

    @Query("SELECT  COUNT(simId) FROM  modemSetting")
    fun getModemRegisterCountValue(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperatorRecord(operatorRecord: OperatorRecord?)

    @Transaction
    @Query("SELECT * FROM operatorRecord where operatorName =:operatorName")
    fun getOperatorUSSD(operatorName: String): OperatorRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGetBalanceManageRecord(getBalanceManageRecord: GetBalanceManageRecord?)

    @Query("DELETE FROM GetBalanceManageRecord")
    fun deleteGetBalanceManageRecord()

    @Query("Select * from GetBalanceManageRecord")
    fun getBalanceTransaction(): List<GetBalanceManageRecord>

    @Query("Select * from GetBalanceManageRecord Where status =LOWER(:status)")
    fun getBalanceTransaction(status: String): List<GetBalanceManageRecord?>

    @Query("select Count(*) from GetBalanceManageRecord where status =LOWER(:status)")
    fun getCountBalanceTransaction(status: String): Int

    @Query("select Count(*) from GetBalanceManageRecord")
    fun getCountBalanceTransaction(): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGetMessageManageRecord(getMessageManageRecord: GetMessageManageRecord)

    @Query("DELETE FROM GetMessageManageRecord")
    fun deleteGetMessageManageRecord()

    @Query("Select * from GetMessageManageRecord")
    fun getMessageTransaction(): List<GetMessageManageRecord>

    @Query("Select * from GetMessageManageRecord Where smsType = :smsType")
    fun getMessageTransaction(smsType: Int): List<GetMessageManageRecord?>

    @Query("select Count(*) from GetMessageManageRecord where smsType =:smsType")
    fun getCountMessageTransaction(smsType: Int): Int

    @Query("select Count(*) from GetMessageManageRecord")
    fun getCountMessageTransaction(): Int
}