package com.jionex.agent.syncManager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jionex.agent.BuildConfig
import com.jionex.agent.data.model.request.MessagesJsonModel
import com.jionex.agent.data.model.request.SMSResultJsonModel
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.data.remote.RetrofitClientInstance
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.roomDB.dao.JionexDao
import com.jionex.agent.roomDB.model.SMSRecord
import com.jionex.agent.utils.Constant
import com.jionex.agent.utils.NetworkHelper
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SMSDataSync(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    var TAG = SMSDataSync::class.java.name
    var rapidxDao: JionexDao? = JionexDatabase.getDatabase(context)?.rapidxDao()
    var userId = ""
    override fun doWork(): Result {
        sendSMSServerData()
        return Result.retry()
    }

    private fun sendSMSServerData() {
        Log.d(TAG, "sendSMSServerData")
        if (SharedPreference(context).getUserId()!=null)
            userId = SharedPreference(context).getUserId()!!
        val syncSMSRecordList = rapidxDao?.getSyncSMSRecord(1)
        val isSyncAllRecord = syncSMSRecordList?.let { isSyncAllRecord(it) };
        Log.d(TAG, "syncRecord$isSyncAllRecord")
        if (isSyncAllRecord == 1) {
            sendBroadCast("Complete")
            syncSMSRecordList?.forEach {
                onStoppedWork(it.messageId)
            }
        } else {
            val dashboardAPI: JionexApiServices =
                RetrofitClientInstance.getRetrofitInstance()!!
                    .create(
                        JionexApiServices::class.java
                    )
            Utility.logs(TAG, "Total Record Need to Sync Size:${syncSMSRecordList?.size}")
            /* syncSMSRecordList?.forEach{
                 GlobalScope.launch(Dispatchers.IO){
                     //uploadRecordToServer(it)
                 }
             }*/

            syncSMSRecordList?.let { sendSyncDataRecord(dashboardAPI, it) }
        }
    }

    private fun isSyncAllRecord(syncResultRecordList: List<SMSRecord>): Int {
        if (syncResultRecordList.isEmpty()) {
            return 1
        } else {
            syncResultRecordList.forEach {
                if (it.syncStatus != 1)
                    return 0
            }
        }
        return 1
    }

    private fun sendSyncDataRecord(
        dashboardAPI: JionexApiServices,
        syncResultRecordList: List<SMSRecord>
    ) {
        if (NetworkHelper(context).isNetworkConnected()) {
            val testResultJsonModelArrayList: ArrayList<SMSResultJsonModel> = ArrayList();
            repeat(syncResultRecordList.size) {
                for (smsRecord in syncResultRecordList) {
                    testResultJsonModelArrayList.add(
                        SMSResultJsonModel(
                            0,
                            smsRecord.messageId,
                            smsRecord.message,
                            smsRecord.senderId,
                            smsRecord.receiver,
                            smsRecord.simSlot,
                            smsRecord.smsType,
                            smsRecord.messageType,
                            smsRecord.deviceId,
                            BuildConfig.VERSION_NAME,
                            smsRecord.date,
                            userId,
                        )
                    )
                }
            }
            Utility.logs(TAG, "sendSyncDataRecord")
            Utility.logs(
                TAG,
                "TestResultJsonModelArrayList:${MessagesJsonModel(testResultJsonModelArrayList)}"
            )
            sendBroadCast("In-Progress")
            dashboardAPI.InsertOrUpdateMultiMessage(
                MessagesJsonModel(testResultJsonModelArrayList)
            )
                .enqueue(object : Callback<JSONObject> {
                    override fun onResponse(
                        call: Call<JSONObject>,
                        response: Response<JSONObject>,
                    ) {
                        if (response.code() === 200) {
                            updateSendDataSyncUp(syncResultRecordList, 1)
                            Utility.syncDataToServer(context)
                        } else {
                            updateSendDataSyncUp(syncResultRecordList, 2)
                        }
                    }

                    override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                        Utility.logs(TAG, "updateSendDataSyncUpError:${call.request()}");
                    }

                })
        } else {
            sendBroadCast("Pending")
            updateSendDataSyncUp(syncResultRecordList, 2)
        }
    }

    private fun updateSendDataSyncUp(resultId: String?, syncStatus: Int) {
        Log.d(TAG, "=SingleUpdateSendDataSyncUp=")
        resultId?.let {
            Utility.logs(TAG, "Update file send resultId:${it}")
            rapidxDao?.updateSyncResultRecord(syncStatus, resultId)
        }
        Log.d(TAG, "Update file send:${syncStatus}")
    }

    private fun updateSendDataSyncUp(syncResultRecordList: List<SMSRecord>, syncStats: Int) {
        Utility.logs(TAG, "=MultipleUpdateSendDataSyncUp=")
        CoroutineScope(Dispatchers.IO).launch {
            syncResultRecordList.forEach { syncResultRecord ->
                syncResultRecord?.syncStatus = syncStats
                syncResultRecord?.messageId?.let {
                    rapidxDao?.updateSyncResultRecord(syncStats, it)
                }
            }
            val isSyncAllRecord = isSyncAllRecord(syncResultRecordList);
            Log.d(TAG, "syncRecord$isSyncAllRecord")
            if (isSyncAllRecord == 1) {
                Log.d(TAG, "size${syncResultRecordList.size}")
                syncResultRecordList.forEach {
                    onStoppedWork(it.messageId)
                }
                sendBroadCast("Complete")
            } else {
                if (NetworkHelper(context).isNetworkConnected())
                    sendBroadCast("In-Progress")
                else
                    sendBroadCast("pending")
            }
        }

    }

    private fun onStoppedWork(messageId: String?) {
        Log.d(TAG, "==onStoppedWork==")
        val tag = "DataSyncRecord_${messageId}"
        Log.d(TAG, "tag:$tag")
        messageId?.let {
            sendBroadCast("Complete")
            WorkManager.getInstance(context).cancelAllWorkByTag(tag)
        }
    }

    private fun sendBroadCast(message: String) {
        SharedPreference(context).setWorkManagerStatus(message)
        val intent = Intent(Constant.ACTION_EVENT_SMS_SYNC)
        intent.putExtra("Action", message)
        context.sendBroadcast(intent)
    }
}