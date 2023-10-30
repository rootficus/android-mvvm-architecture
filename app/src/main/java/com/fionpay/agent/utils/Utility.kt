package com.fionpay.agent.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import com.fionpay.agent.R
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import java.util.regex.Pattern


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
        Log.d(tag, s)
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

    fun convertTransactionDate(utcTime: String?): String? {
        var utcTimestamp = ""
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date = utcTime?.let { dateFormat.parse(it) }
            val localDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            // You can set the desired local time zone here if it's different from the device's default.
            // localDateFormat.timeZone = TimeZone.getTimeZone("Your_Local_Time_Zone")
            val localTime = date?.let { localDateFormat.format(it) }
            if (localTime != null) {
                utcTimestamp = localTime
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return utcTimestamp
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

    fun convertCurrencyFormat(currency: Double): String {
        Log.d("convertCurrencyFormat", "::${currency}")
        val formatter = DecimalFormat("###,###,##0.00")
        Log.d("convertCurrencyFormat", "::${formatter.format(currency)}")
        return formatter.format(currency)
    }

    fun isEmailValid(email: String): Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun callCustomToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun currentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun dateBeforeOneMonth(): String? {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }
    private fun getNewDayOfMonth(day: Int): String {
        val calendar = Calendar.getInstance()
        var dayOfMonth = day.toString()
        val simpleDateFormat = SimpleDateFormat("dd")
        calendar.time = simpleDateFormat.parse(dayOfMonth) as Date
        calendar.add(Calendar.DATE, 7)
        dayOfMonth = simpleDateFormat.format(calendar.time)
        return dayOfMonth
    }

    fun textAsBitmap(text: String): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 50f
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.LEFT
        val textTypeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.typeface = textTypeface

        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.5f).toInt() // round
        val height = (baseline + paint.descent() + 0.5f).toInt()

        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawText(text, 0f, baseline, paint)
        return image
    }


}