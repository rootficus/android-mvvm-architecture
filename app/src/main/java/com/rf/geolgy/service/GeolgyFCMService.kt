package com.rf.geolgy.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rf.geolgy.roomDB.GeolgyDatabase
import com.rf.geolgy.utils.SharedPreference

open class GeolgyFCMService : FirebaseMessagingService() {
    var TAG: String = GeolgyFCMService::class.java.name

    lateinit var geolgyDatabase: GeolgyDatabase

    override fun onCreate() {
        super.onCreate()
        geolgyDatabase = GeolgyDatabase.getDatabase(applicationContext)
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