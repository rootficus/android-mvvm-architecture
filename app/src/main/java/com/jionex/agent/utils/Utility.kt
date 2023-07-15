package com.jionex.agent.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.jionex.agent.R
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.roomDB.dao.JionexDao
import com.jionex.agent.roomDB.model.SMSRecord
import com.jionex.agent.syncManager.SMSDataSync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

object Utility {

    fun showCommonProgressDialog(context: Context): Dialog {
        val views: View = LayoutInflater.from(context).inflate(R.layout.item_progress_bar, null)
        return Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setContentView(views)
            setCancelable(false)
        }
    }

    fun generateUUID(): String {
        val uuid: UUID = UUID.randomUUID()
        return uuid.toString()
    }

    fun getFormatTimeWithTZ(currentTime: Date?): String? {
        val timeZoneDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return timeZoneDate.format(currentTime)
    }

    fun logs(tag: String, s: String) {
        Log.d(tag,s)
    }

    fun syncDataToServer(context: Context) {
        var rapidxDao : JionexDao? = JionexDatabase.getDatabase(context)?.rapidxDao()
        rapidxDao.let {
            GlobalScope.launch(Dispatchers.IO){
                var getsyncResult = rapidxDao?.getSyncSMSRecord(1)
                if (getsyncResult?.size!!>0){
                    syncSMSRecordProgress(context,getsyncResult[0])
                }
            }
        }
    }

    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    fun syncSMSRecordProgress(context: Context, smsRecord: SMSRecord) {
        logs("Utility", "=syncRecordSessionProgress=")
        val constraintBuilder = Constraints.Builder()
        constraintBuilder.setRequiredNetworkType(NetworkType.CONNECTED)
        constraintBuilder.setRequiresBatteryNotLow(true)
        constraintBuilder.setRequiresStorageNotLow(true)
        val inputData = Data.Builder()
            .putString(Constant.MESSAGE_ID, smsRecord.messageId)
            .build()
        var tag = "DataSyncRecord_${smsRecord.messageId!!}"
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(SMSDataSync::class.java, 10, TimeUnit.SECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                ).setInputData(inputData).addTag(tag).build()
        val manager = WorkManager.getInstance(context)
        if (manager != null) {
            manager.cancelAllWorkByTag(tag)
            logs("Utitlity", "SyncRecordProgress Tag:$tag")
            manager.enqueue(periodicWorkRequest)
            manager.enqueueUniquePeriodicWork(
                tag,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }

    fun isB2BMessages(message: String): Boolean {
        return if (!message.contains("You have received Cash-out"))
            message.contains(
                "B2B",
                true
            ) || message.contains("B2B Received") || message.contains("You have received", true)
        else
            false
    }

    fun isCashInMessages(message: String): Boolean {
        return message.contains("Cash In",true) || message.contains("Cash-in") || message.contains("B2C: Cash-In",true)
    }

    fun isCashOutMessages(message: String): Boolean {
        return message.contains("Cash Out",true) || message.contains("You have received Cash-out",true) || message.contains("B2C: Cash-Out",true)
    }

    fun getMessageType(message: String): String {
        if(isB2BMessages(message)){
            return if (message.contains("received",true) || message.contains("B2B Cash-in",true)|| message.contains("Cash-Out"))
                "B2B Received"
            else
                "B2B Transfer"
        }else if (isCashInMessages(message)){
            return "Cash In"
        }else if (isCashOutMessages(message)){
            return "Cash Out"
        }
        return ""
    }


}