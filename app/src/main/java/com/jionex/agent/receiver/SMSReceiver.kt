package com.jionex.agent.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import android.telephony.SubscriptionManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.roomDB.dao.JionexDao
import com.jionex.agent.roomDB.model.ModemSetting
import com.jionex.agent.roomDB.model.SMSRecord
import com.jionex.agent.utils.Constant
import com.jionex.agent.utils.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date


class SMSReceiver : BroadcastReceiver() {
    private var rapidxDao: JionexDao? = null
    val TAG = SMSRecord::class.java.name
    override fun onReceive(context: Context?, intent: Intent?) {
        if (rapidxDao == null) {
            rapidxDao = context?.let { JionexDatabase.getDatabase(it)?.rapidxDao() }
        }
        Log.d("SMSReceiver",":SMSReceiver:")
        val bundle = intent?.extras
        if (bundle != null) {
            val sub = bundle.getInt("subscription", -1)
            if (context?.let {
                    ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_PHONE_STATE
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val manager = SubscriptionManager.from(context)
            var subscriptionInfo = manager.getActiveSubscriptionInfo(sub)
            val simRecord: ModemSetting? = rapidxDao?.getModemSetting(subscriptionInfo.subscriptionId)

            GlobalScope.launch(Dispatchers.IO) {
                if (simRecord!=null){
                    val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    if (msgs!=null && msgs.isNotEmpty()){
                        var currentMessage = msgs[0]
                        var message = ""
                        msgs.forEach {
                            message += it.messageBody
                        }
                        if (message.isEmpty()) {
                            message = currentMessage.messageBody
                        }
                        message.replace("\n"," ")
                        if (currentMessage != null) {
                            var originalAddress = currentMessage.displayOriginatingAddress
                            Log.d("OriginalAddress", "$originalAddress")
                            /*if (originalAddress.contains("+919056914105")
                                ||originalAddress.contains("+919992593105")
                                ||originalAddress.contains("+918360177468")){
                                originalAddress = "bKash"
                            }*/
                            Log.d(TAG, "$originalAddress")

                            var simType = Constant.SMSType.UNKNOWN
                            if (Utility.isCashInMessages(message)) {
                                simType = Constant.SMSType.CashIn
                            } else if (Utility.isCashOutMessages(message)) {
                                simType = Constant.SMSType.CashOut
                            } else if (Utility.isB2BMessages(message)) {
                                simType = Constant.SMSType.B2B
                            }
                            var messageType = ""
                            if (simType != Constant.SMSType.UNKNOWN) {
                                messageType = Utility.getMessageType(message)
                            }

                            var messageId = Utility.generateUUID()
                            val smsRecord = SMSRecord(
                                messageId,
                                originalAddress,
                                message,
                                simRecord.operator,
                                simRecord.phoneNumber,
                                simRecord.deviceId,
                                simRecord.type,
                                Utility.getFormatTimeWithTZ(Date(currentMessage.timestampMillis)),
                                simType.value,
                                messageType,
                                0
                            )
                            rapidxDao?.insertSMSRecord(smsRecord)
                            context?.let { Utility.syncDataToServer(it) }
                        }
                    }
                }
            }
        }
    }
}