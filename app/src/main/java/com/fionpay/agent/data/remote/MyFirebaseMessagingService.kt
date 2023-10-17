package com.fionpay.agent.data.remote

import android.util.Log
import com.fionpay.agent.utils.SharedPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if the message contains data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            // Handle your data here
        }

        // Check if the message contains notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // Handle your notification here
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        val sharedPreference  = SharedPreference(applicationContext)
        sharedPreference.setDeviceToken(token)
        // If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the FCM registration token to your server.
        // Send the token to your server if necessary.
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
