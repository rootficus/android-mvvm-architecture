package com.rf.accessAli.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rf.accessAli.roomDB.AccessAliDatabase
import com.rf.accessAli.utils.SharedPreference

open class AccessAliFCMService : FirebaseMessagingService() {
    var TAG: String = AccessAliFCMService::class.java.name

    lateinit var accessAliDatabase: AccessAliDatabase

    override fun onCreate() {
        super.onCreate()
        accessAliDatabase = AccessAliDatabase.getDatabase(applicationContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
        Log.d(TAG, "From: ${remoteMessage.notification.toString()}")

        // Check if the message contains data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        val sharedPreference = SharedPreference(applicationContext)
        sharedPreference.setPushToken(token)
    }
}