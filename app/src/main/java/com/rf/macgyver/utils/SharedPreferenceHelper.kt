package com.rf.macgyver.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rf.macgyver.data.model.request.ImageData

/**
 * Akash.Singh
 * RootFicus.
 */
class SharedPreferenceHelper(context: Context){
    private val PREFS_NAME = "macgyver_prefs"
    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

    fun storeImageDataList( dataList: List<ImageData>) {
        val gson = Gson()
        val json = gson.toJson(dataList)
        pref.edit().putString("imageList", json).apply()
    }

    fun retrieveImageDataList(): List<ImageData> {
        val gson = Gson()
        val json = pref.getString("imageList", null)
        val type = object : TypeToken<List<ImageData>>() {}.type
        return gson.fromJson(json, type)
    }

   /* private const val PREFS_NAME = "my_prefs"
    private const val KEY_IMAGE_PREFIX = "image_"
    private const val KEY_COUNT_PREFIX = "count_"

    fun saveImages(context : Context, imageIndex: Int, image: Int, count: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val imageBm = intToBitmap(image)
        val imageString = imageBm?.let { bitmapToBase64(it) }
        editor.putString(KEY_IMAGE_PREFIX + imageIndex, imageString)

        // Save count
        editor.putInt(KEY_COUNT_PREFIX + imageIndex, count)
        editor.apply()
    }

    fun getImageData(context: Context, imageIndex: Int): Pair<Int?, Int> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val imageString = prefs.getString(KEY_IMAGE_PREFIX + imageIndex, null)
        val image = base64ToBitmap(imageString)
        val imageInt = image?.let { bitmapToInt(it) }

        // Retrieve count
        val count = prefs.getInt(KEY_COUNT_PREFIX + imageIndex, 0)

        return Pair(imageInt, count)
    }
    fun getTotalEntries(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all
        return allEntries.size
    }
    fun isDataStored(context: Context, key: Int): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.contains(key.toString())
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun base64ToBitmap(base64String: String?): Bitmap? {
        return if (base64String != null) {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } else {
            null
        }
    }
    fun intToBitmap(imageInt: Int): Bitmap? {
        val base64String = imageInt.toString()

        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun bitmapToInt(bitmap: Bitmap): Int {
        val bytes = bitmap.toByteArray()
        val md5Hash = bytes.md5()
        return md5Hash.hashCode()
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun ByteArray.md5(): ByteArray {
        val digest = MessageDigest.getInstance("MD5")
        return digest.digest(this)
    }*/


}

