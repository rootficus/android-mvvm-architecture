package com.jionex.agent.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SubscriptionManager
import android.util.Log
import androidx.core.content.ContextCompat


class SimChangeReceiver : BroadcastReceiver() {
    private val TAG = "SimChangeReceiver"
    private var currentSimSlots: List<Int> = ArrayList()

    override fun onReceive(context: Context?, intent: Intent?) {

        if (ContextCompat.checkSelfPermission(
                context!!, Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val simState = intent!!.getStringExtra("ss")
            Log.d(TAG, "" + simState)
            val subscriptionManager = SubscriptionManager.from(context)
            val activeSubscriptionList = subscriptionManager.activeSubscriptionInfoList
            if (activeSubscriptionList != null) {
                val updatedSimSlots: ArrayList<Int> = ArrayList()
                for (subscriptionInfo in activeSubscriptionList) {
                    val slotIndex = subscriptionInfo.simSlotIndex
                    updatedSimSlots.add(slotIndex)
                    if (!currentSimSlots.contains(slotIndex)) {
                        // New SIM card inserted
                        val carrierName = subscriptionInfo.carrierName.toString()
                        Log.d(TAG, "New SIM card inserted in slot $slotIndex ($carrierName)")
                    }
                }

                for (slotIndex in currentSimSlots) {
                    if (!updatedSimSlots.contains(slotIndex)) {
                        // SIM card removed
                        Log.d(TAG, "SIM card removed from slot $slotIndex")
                    }
                }

                currentSimSlots = updatedSimSlots
            }
        } else {
            Log.d(TAG, "READ_PHONE_STATE permission not granted");
        }


    }
}