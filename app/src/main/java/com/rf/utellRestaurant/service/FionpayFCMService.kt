package com.rf.utellRestaurant.service

import android.content.Context
import android.content.Intent
import android.util.Log
import com.rf.utellRestaurant.App
import com.rf.utellRestaurant.roomDB.FionDatabase
import com.rf.utellRestaurant.roomDB.model.NotificationMessageBody
import com.rf.utellRestaurant.roomDB.model.NotificationRecord
import com.rf.utellRestaurant.utils.Constant
import com.rf.utellRestaurant.utils.Constant.AGENT_REQUEST_TRANSACTION_BOTS
import com.rf.utellRestaurant.utils.Constant.UTELL_ACTION
import com.rf.utellRestaurant.utils.Constant.HOME_REQUEST_NOTIFICATION_COUNT
import com.rf.utellRestaurant.utils.Constant.MODEM_STATUS_CHANGE_ACTIONS
import com.rf.utellRestaurant.utils.NotificationUtils
import com.rf.utellRestaurant.utils.SharedPreference
import com.rf.utellRestaurant.utils.Utility
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.rf.utellRestaurant.R

open class UtellFCMService : FirebaseMessagingService() {
    var TAG: String = UtellFCMService::class.java.name

    lateinit var fionDatabase: FionDatabase

    override fun onCreate() {
        super.onCreate()
        fionDatabase = FionDatabase.getDatabase(applicationContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
        Log.d(TAG, "From: ${remoteMessage.notification.toString()}")

        // Check if the message contains data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            if (remoteMessage.data.isNotEmpty()) {
                val notificationType =
                    remoteMessage.data.getOrDefault("notification_type", "Unknown")
                setNotificationCount(notificationType)
                when (notificationType) {
                    Constant.NotificationType.MODEM_STATUS_CHANGE.param -> {
                        insertNotificationMessage(remoteMessage, notificationType)
                    }

                    Constant.NotificationType.ADD_BALANCE_AGENT.param -> {
                        insertNotificationMessage(remoteMessage, notificationType)
                    }

                    Constant.NotificationType.DEPOSIT_REQUEST.param -> {
                        insertNotificationMessage(remoteMessage, notificationType)
                    }

                    Constant.NotificationType.WITHDRAWAL_REQUEST.param -> {
                        insertNotificationMessage(remoteMessage, notificationType)
                    }

                    Constant.NotificationType.REFUND_REQUEST.param -> {
                        insertNotificationMessage(remoteMessage, notificationType)
                    }
                }
            }

        }
    }

    private fun setNotificationCount(notificationType: String) {
        val sharedPreference = SharedPreference(applicationContext)
        when (notificationType) {
            Constant.NotificationType.MODEM_STATUS_CHANGE.param -> {
                val notificationCount = sharedPreference.getModemChangeStatusNotificationCount() + 1
                sharedPreference.setModemChangeStatusNotificationCount(notificationCount)
            }

            Constant.NotificationType.ADD_BALANCE_AGENT.param -> {
                val notificationCount = sharedPreference.getAddBalanceModemNotificationCount() + 1
                sharedPreference.setAddBalanceModemNotificationCount(notificationCount)
            }

            Constant.NotificationType.DEPOSIT_REQUEST.param -> {
                val notificationCount =
                    sharedPreference.getDepositRequestModemNotificationCount() + 1
                sharedPreference.setDepositRequestModemNotificationCount(notificationCount)
            }

            Constant.NotificationType.WITHDRAWAL_REQUEST.param -> {
                val notificationCount =
                    sharedPreference.getWithdrawalRequestModemNotificationCount() + 1
                sharedPreference.setWithdrawalRequestModemNotificationCount(notificationCount)
            }

            Constant.NotificationType.REFUND_REQUEST.param -> {
                val notificationCount =
                    sharedPreference.getRefundRequestModemNotificationCount() + 1
                sharedPreference.setRefundRequestModemNotificationCount(notificationCount)
            }
        }
    }

    private fun insertNotificationMessage(remoteMessage: RemoteMessage, notificationType: String) {
        val notificationMessageBody = Gson().fromJson(
            remoteMessage.data.getValue("message"),
            NotificationMessageBody::class.java
        )
        if (Utility.isNotificationExits(
                fionDatabase.fioDao(),
                notificationMessageBody.id
            ) != true
        ) {
            notificationMessageBody.let {
                val notificationRecord = NotificationRecord(
                    id = it.id, notificationType = notificationType, messageBody = it.messageBody,
                    isRead = false,
                    isSend = false,
                    createdAt = it.date
                )
                fionDatabase.fioDao()?.insertNotification(notificationRecord)
                sendBroadCast(applicationContext, notificationRecord)
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        val sharedPreference = SharedPreference(applicationContext)
        sharedPreference.setPushToken(token)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    private fun sendBroadCast(context: Context, notificationRecord: NotificationRecord) {
        if (App.isAppRunning) {
            if (notificationRecord.notificationType == Constant.NotificationType.MODEM_STATUS_CHANGE.param
                || notificationRecord.notificationType == Constant.NotificationType.ADD_BALANCE_AGENT.param
            ) {
                val intent = Intent(MODEM_STATUS_CHANGE_ACTIONS)
                intent.putExtra(UTELL_ACTION, notificationRecord.messageBody)
                context.sendBroadcast(intent)
            } else {
                val intent = Intent(AGENT_REQUEST_TRANSACTION_BOTS)
                intent.putExtra(UTELL_ACTION, notificationRecord.messageBody)
                context.sendBroadcast(intent)
            }

            val intent = Intent(HOME_REQUEST_NOTIFICATION_COUNT)
            intent.putExtra(UTELL_ACTION, notificationRecord.messageBody)
            context.sendBroadcast(intent)
        } else {
            NotificationUtils.showNotification(
                applicationContext,
                getTitle(notificationRecord.notificationType!!),
                notificationRecord.messageBody!!,
                notificationRecord.id
            )
        }
    }

    private fun getTitle(notificationType: String): String {
        return when (notificationType) {
            Constant.NotificationType.MODEM_STATUS_CHANGE.param -> getString(R.string.modem_status_change)
            Constant.NotificationType.ADD_BALANCE_AGENT.param -> getString(R.string.balance_change)
            Constant.NotificationType.DEPOSIT_REQUEST.param -> getString(R.string.deposit_request)
            Constant.NotificationType.WITHDRAWAL_REQUEST.param -> getString(R.string.withdrawal_request)
            Constant.NotificationType.REFUND_REQUEST.param -> getString(R.string.refund_request)
            else -> getString(R.string.app_name)
        }
    }
}