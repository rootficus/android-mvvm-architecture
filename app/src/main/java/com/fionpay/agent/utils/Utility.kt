package com.fionpay.agent.utils

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.fionpay.agent.R
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

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

    fun convertUtc2Local(utcTime: String?): String? {
        var time = ""
        if (utcTime != null) {
            val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var gpsUTCDate: Date? = null //from  ww  w.j  a va 2 s  . c  o  m
            try {
                gpsUTCDate = utcFormatter.parse(utcTime)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val localFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
            localFormatter.timeZone = TimeZone.getDefault()
            assert(gpsUTCDate != null)
            time = localFormatter.format(gpsUTCDate?.time)
        }
        return time
    }

    fun convertTodayUtc2Local(utcTime: String?): String? {
        var time = ""
        if (utcTime != null) {
            val utcFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var gpsUTCDate: Date? = null //from  ww  w.j  a va 2 s  . c  o  m
            try {
                gpsUTCDate = utcFormatter.parse(utcTime)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val localFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
            localFormatter.timeZone = TimeZone.getDefault()
            assert(gpsUTCDate != null)
            time = localFormatter.format(gpsUTCDate?.time)
        }
        return time
    }

    fun convertCurrencyFormat(currency: Double) : String{
        Log.d("convertCurrencyFormat","::${currency}")
        val formatter = DecimalFormat("###,###,##0.00")
        Log.d("convertCurrencyFormat","::${formatter.format(currency)}")
        return formatter.format(currency)
    }

}