package com.rf.accessAli.utils

import android.content.Context
import com.rf.accessAli.utils.Constant.EDIT2_TEXT_10
import com.rf.accessAli.utils.Constant.EDIT_TEXT_10
import com.rf.accessAli.utils.Constant.EDIT_TEXT_14
import com.rf.accessAli.utils.Constant.EDIT_TEXT_6
import com.rf.accessAli.utils.Constant.EDIT_TEXT_7
import com.rf.accessAli.utils.Constant.EDIT_TEXT_8
import com.rf.accessAli.utils.Constant.EXPIRE_HOUR
import com.rf.accessAli.utils.Constant.PREFS_IS_LOGIN
import com.rf.accessAli.utils.Constant.PREF_PASSWORD
import com.rf.accessAli.utils.Constant.PREF_PUSH_TOKEN
import com.rf.accessAli.utils.Constant.PREF_SIGN_IN_LIST
import com.rf.accessAli.utils.Constant.PREF_SIGN_IN_MODEL
import com.rf.accessAli.utils.Constant.PREF_TOKEN

/**
 * Akash.Singh
 * RootFicus.
 */
class SharedPreference(context: Context) {
    private val PREFS_NAME = "pref_file"
    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

    fun setToken(token: String) {
        pref.edit().putString(PREF_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return pref.getString(PREF_TOKEN, "")
    }

    fun setPushToken(token: String) {
        pref.edit().putString(PREF_PUSH_TOKEN, token).apply()
    }

}