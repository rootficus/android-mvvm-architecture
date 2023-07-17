package com.jionex.agent.utils

import android.content.Context
import com.jionex.agent.utils.Constant.PREFS_IS_LOGIN
import com.jionex.agent.utils.Constant.PREF_PASSWORD
import com.jionex.agent.utils.Constant.PREF_TOKEN

/**
 * Akash.Singh
 * RootFicus.
 */
class SharedPreference(context: Context) {
    private val PREFS_NAME = "jionex_modem_pref_file"
    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

    fun getWorkMangerStatus(): String? {
        return pref.getString("WorkManagerStatus", "")
    }

    fun setWorkManagerStatus(customerId: String) {
        pref.edit().putString("WorkManagerStatus", customerId).apply()
    }

    fun setUserId(userId: String){
        pref.edit().putString(Constant.PREF_USER_ID,userId).apply()
    }

    fun getUserId() :String?{
        return pref.getString(Constant.PREF_USER_ID,"")
    }

    fun setUserName(userName: String){
        pref.edit().putString(Constant.PREF_USER_NAME,userName).apply()
    }

    fun getUserName() :String?{
        return pref.getString(Constant.PREF_USER_NAME,"")
    }

    fun setEmail(email: String){
        pref.edit().putString(Constant.PREF_EMAIL,email).apply()
    }

    fun getEmail() :String?{
        return pref.getString(Constant.PREF_EMAIL,"")
    }

    fun getPassword():String?{
        return pref.getString(Constant.PREF_PASSWORD,"")
    }

    fun setPhoneNumber(phoneNumber: String){
        pref.edit().putString(Constant.PREF_PHONE_NUMBER,phoneNumber).apply()
    }

    fun getPhoneNumber() :String?{
        return pref.getString(Constant.PREF_PHONE_NUMBER,"")
    }

    fun setFullName(fullName: String){
        pref.edit().putString(Constant.PREF_FULL_NAME,fullName).apply()
    }

    fun getFullName() :String?{
        return pref.getString(Constant.PREF_FULL_NAME,"")
    }

    fun setPinCode(userId: Int){
        pref.edit().putInt(Constant.PREF_PIN_CODE,userId).apply()
    }

    fun getPinCode() :Int?{
        return pref.getInt(Constant.PREF_PIN_CODE,0)
    }

    fun setCountry(userId: String){
        pref.edit().putString(Constant.PREF_COUNTRY,userId).apply()
    }

    fun getCountry() :String?{
        return pref.getString(Constant.PREF_COUNTRY,"")
    }

    fun setParentId(parentId: String){
        pref.edit().putString(Constant.PREF_PARENT_ID,parentId).apply()
    }

    fun getParentId() :String?{
        return pref.getString(Constant.PREF_PARENT_ID,"")
    }

    fun setUserRole(userRole: String){
        pref.edit().putString(Constant.PREF_USER_ROLE,userRole).apply()
    }

    fun getUserRole() :String?{
        return pref.getString(Constant.PREF_USER_ROLE,"")
    }

    fun getModemSetupConfirmation(): Boolean {
        return pref.getBoolean(Constant.PREF_MODEM_SETUP,false)
    }

    fun setModemSetupConfirmation(modemSetup: Boolean){
        pref.edit().putBoolean(Constant.PREF_MODEM_SETUP,modemSetup).apply()
    }
    fun setToken(token: String) {
        pref.edit().putString(PREF_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return pref.getString(PREF_TOKEN, "")
    }

    fun setIsLogin(isLogin: Boolean) {
        pref.edit().putBoolean(PREFS_IS_LOGIN,isLogin).apply()
    }

    fun isLogin() : Boolean{
        return pref.getBoolean(PREFS_IS_LOGIN,false)
    }

    fun setPassword(password: String?) {
        pref.edit().putString(PREF_PASSWORD,password).apply()
    }
}