package com.rf.macgyver.roomDB.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rf.macgyver.roomDB.model.DailyReporting
import com.rf.macgyver.roomDB.model.IncidentReport
import com.rf.macgyver.roomDB.model.InspectionForm
import com.rf.macgyver.roomDB.model.LoginDetails
import com.rf.macgyver.roomDB.model.OperatorRecord

@Dao
interface MagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoginDetails(loginDetails: LoginDetails)

    @Update
    fun updateLoginDetails(loginDetails: LoginDetails): Int

    @Query("UPDATE loginDetails SET companyIndustry = :value1, noOfVehicles = :value2, userRole = :value3  WHERE emailId = :id")
    fun updateCompanyInfo(id: String?, value1: String?, value2: String?, value3 : String?)
    @Query("UPDATE loginDetails SET motiveHVI = :value1 WHERE emailId = :id")
    fun updateMotiveHVI(id: String?,  value1 : ArrayList<String>)

    @Transaction
    @Query("SELECT * FROM loginDetails WHERE emailId=:email")
    fun getLoginDetails(email: String): LoginDetails
    @Transaction
    @Query("SELECT * FROM loginDetails WHERE uniqueToken=:uniqueToken")
    fun getLoginDetailsUsingToken(uniqueToken: String): LoginDetails

    @Update
    fun updateDailyReporting(dailyReporting: DailyReporting): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDailyReporting(dailyReporting: DailyReporting)

    @Transaction
    @Query("SELECT * FROM dailyReporting WHERE uniqueToken=:uniqueToken")
    fun getDailyReportingData(uniqueToken: String): List<DailyReporting>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncidentReport(incidentReport: IncidentReport)

    @Update
    fun updateIncidentReport(incidentReport: IncidentReport): Int

    @Transaction
    @Query("SELECT * FROM incidentReport WHERE uniqueToken=:uniqueToken")
    fun getIncidentReport(uniqueToken: String): List<IncidentReport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInspectionForm(inspectionForm: InspectionForm)

    @Update
    fun updateInspectionForm(inspectionForm: InspectionForm): Int

    @Transaction
    @Query("SELECT * FROM inspectionForm WHERE uniqueToken=:uniqueToken")
    fun getInspectionForm(uniqueToken : String): InspectionForm
/*
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
    fun updateSyncResultRecord(syncStats: Int, messageId: String)*/

    /* @Transaction
    @Query("SELECT * FROM modemSetting")
    fun getModemSetting(): List<ModemSetting>?

    @Transaction
    @Query("SELECT * FROM modemSetting where simId =:simId")
    fun getModemSetting(simId: Int): ModemSetting

    @Query("SELECT  COUNT(simId) FROM  modemSetting")
    fun getModemRegisterCountValue(): Int
*/
/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperatorRecord(operatorRecord: OperatorRecord)

    @Transaction
    @Query("SELECT * FROM operatorRecord where operatorName =:operatorName")
    fun getOperatorUSSD(operatorName: String): OperatorRecord*/

/*
    @Query("SELECT EXISTS (SELECT * FROM notificationRecord WHERE id = :id)")
    fun isNotificationExits(id: String?): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notificationRecord: NotificationRecord)*/

}