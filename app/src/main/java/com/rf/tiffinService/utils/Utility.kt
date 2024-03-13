package com.rf.tiffinService.utils

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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.rf.tiffinService.R
import com.rf.tiffinService.data.model.Menus
import com.rf.tiffinService.data.model.Order
import com.rf.tiffinService.roomDB.dao.UtellDao
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

    fun getCurrentDateTimeFormat(): String? {
        val timeZoneDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return timeZoneDate.format(Date())
    }

    fun logs(tag: String, s: String) {
        Log.d(tag, s)
    }

    fun convertUtc2Local(utcTime: String?): String {
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

    fun convertTransactionDate(utcTime: String?): String {
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

    fun convertTodayUtc2Local(utcTime: String?): String {
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

    fun isNotificationExits(utellDao: UtellDao?, id: String): Boolean? {
        return utellDao?.isNotificationExits(id)
    }

    val currencySymbolBD = "৳"
    fun convertCurrencyFormat(currency: Float): String {
        Log.d("convertCurrencyFormat", "::${currency}")
        val formatter = DecimalFormat("###,###,##0.00")
        Log.d("convertCurrencyFormat", "::${formatter.format(currency)}")
        return currencySymbolBD + formatter.format(currency)
    }

    fun convertDigitalCurrencyFormat(currency: Float): String {
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

    fun getPushToken(activity: Context) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        Utility::class.java.name,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = "FCM TOKEN: $token"
                Log.d(Utility::class.java.name, msg)
                val sharedPreference = SharedPreference(activity)
                sharedPreference.setPushToken(token)
            })
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
        val simpleDateFormat = SimpleDateFormat("dd", Locale.getDefault())
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

    fun generateSampleOrders(): ArrayList<Order> {
        val orders: ArrayList<Order> = arrayListOf()
        orders.add(
            Order(
                name = "Akash Singh",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Akash@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Completed",
                total = "\$500",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Shehbaz Ali",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Shehbaz@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Preparing",
                total = "\$400",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Javed Khan",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Javed@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Completed",
                total = "\$540",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Rizwan",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Rizwan@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Preparing",
                total = "\$504",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Simran",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Simran@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Completed",
                total = "\$600",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Vibhuti",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Vibhuti@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Completed",
                total = "\$560",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Harshal",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Harshal@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Preparing",
                total = "\$505",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Rohan",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Rohan@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Completed",
                total = "\$550",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )
        orders.add(
            Order(
                name = "Manav",
                orderNumber = "12345",
                date = "02 Jan 2024 06:38 AM",
                email = "Manav@gmail.com",
                address = "RootFicus",
                mobile = "9876543210",
                type = "Dine",
                status = "Preparing",
                total = "\$506",
                comment = "No Comment",
                menus = arrayListOf(Menus(itemName = "Burger", quantity = "5", price = "$25"),
                    Menus(itemName = "Sandwitch", quantity = "1", price = "$5"),
                    Menus(itemName = "Malai Tikka ", quantity = "2", price = "$15"),
                    Menus(itemName = "Soyea Chap", quantity = "3", price = "$22"),
                    Menus(itemName = "French Fires", quantity = "4", price = "$28"))
            )
        )

        return orders
    }


}