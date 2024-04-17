package com.rf.geolgy.utils

import android.content.Context
import com.rf.geolgy.utils.Constant.EDIT2_TEXT_10
import com.rf.geolgy.utils.Constant.EDIT_TEXT_10
import com.rf.geolgy.utils.Constant.EDIT_TEXT_14
import com.rf.geolgy.utils.Constant.EDIT_TEXT_6
import com.rf.geolgy.utils.Constant.EDIT_TEXT_7
import com.rf.geolgy.utils.Constant.EDIT_TEXT_8
import com.rf.geolgy.utils.Constant.EXPIRE_HOUR
import com.rf.geolgy.utils.Constant.PREFS_IS_LOGIN
import com.rf.geolgy.utils.Constant.PREF_PASSWORD
import com.rf.geolgy.utils.Constant.PREF_PUSH_TOKEN
import com.rf.geolgy.utils.Constant.PREF_SIGN_IN_LIST
import com.rf.geolgy.utils.Constant.PREF_SIGN_IN_MODEL
import com.rf.geolgy.utils.Constant.PREF_TOKEN

/**
 * Akash.Singh
 * RootFicus.
 */
class SharedPreference(context: Context) {
    private val PREFS_NAME = "fion_modem_pref_file"
    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

    fun setUserId(userId: String) {
        pref.edit().putString(Constant.PREF_USER_ID, userId).apply()
    }

    fun getPassword(): String? {
        return pref.getString(PREF_PASSWORD, "")
    }

    fun setPhoneNumber(phoneNumber: String) {
        pref.edit().putString(Constant.PREF_PHONE_NUMBER, phoneNumber).apply()
    }

    fun getSignInDataModel(): String? {
        return pref.getString(PREF_SIGN_IN_MODEL, "")
    }

    fun setSignInDataModel(model: String?) {
        pref.edit().putString(PREF_SIGN_IN_MODEL, model).apply()
    }

    fun getSignInList(): String? {
        return pref.getString(PREF_SIGN_IN_LIST, "")
    }

    fun setSignInList(model: String?) {
        pref.edit().putString(PREF_SIGN_IN_LIST, model).apply()
    }

    fun setFullName(fullName: String) {
        pref.edit().putString(Constant.PREF_FULL_NAME, fullName).apply()
    }

    fun setPinCode(userId: String) {
        pref.edit().putString(Constant.PREF_PIN_CODE, userId).apply()
    }

    fun setToken(token: String) {
        pref.edit().putString(PREF_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return pref.getString(PREF_TOKEN, "")
    }

    fun setPushToken(token: String) {
        pref.edit().putString(PREF_PUSH_TOKEN, token).apply()
    }

    fun setIsLogin(isLogin: Boolean) {
        pref.edit().putBoolean(PREFS_IS_LOGIN, isLogin).apply()
    }

    fun isLogin(): Boolean {
        return pref.getBoolean(PREFS_IS_LOGIN, false)
    }

    fun setPassword(password: String?) {
        pref.edit().putString(PREF_PASSWORD, password).apply()
    }

    fun setEditText6(token: String) {
        pref.edit().putString(EDIT_TEXT_6, token).apply()
    }

    fun getEditText6(): String? {
        return pref.getString(EDIT_TEXT_6, "")
    }

    fun setEditText7(token: String) {
        pref.edit().putString(EDIT_TEXT_7, token).apply()
    }

    fun getEditText7(): String? {
        return pref.getString(EDIT_TEXT_7, "")
    }

    fun setEditText8(token: Int) {
        pref.edit().putInt(EDIT_TEXT_8, token).apply()
    }

    fun getEditText8(): Int {
        return pref.getInt(EDIT_TEXT_8, 0)
    }

    fun setEditText10(token: String) {
        pref.edit().putString(EDIT_TEXT_10, token).apply()
    }

    fun getEditText10(): String? {
        return pref.getString(EDIT_TEXT_10, "")
    }

    fun setEdit2Text10(token: String) {
        pref.edit().putString(EDIT2_TEXT_10, token).apply()
    }

    fun getEdit2Text10(): String? {
        return pref.getString(EDIT2_TEXT_10, "")
    }

    fun setEditText14(token: String) {
        pref.edit().putString(EDIT_TEXT_14, token).apply()
    }

    fun getEditText14(): String? {
        return pref.getString(EDIT_TEXT_14, "")
    }
    fun setExpireHour(hour: Int) {
        pref.edit().putInt(EXPIRE_HOUR, hour).apply()
    }

    fun getExpireHour(): Int? {
        return pref.getInt(EXPIRE_HOUR, 1)
    }


}