package com.fionpay.agent.utils

import android.content.Context
import android.util.Log
import com.fionpay.agent.utils.Constant.PREFS_IS_LOGIN
import com.fionpay.agent.utils.Constant.PREF_BL_APPROVED
import com.fionpay.agent.utils.Constant.PREF_BL_DANGER
import com.fionpay.agent.utils.Constant.PREF_BL_PENDING
import com.fionpay.agent.utils.Constant.PREF_BL_REJECTED
import com.fionpay.agent.utils.Constant.PREF_BL_SUCCESS
import com.fionpay.agent.utils.Constant.PREF_PASSWORD
import com.fionpay.agent.utils.Constant.PREF_TODAY_TRANSACTIONS
import com.fionpay.agent.utils.Constant.PREF_TODAY_TRX_AMOUNT
import com.fionpay.agent.utils.Constant.PREF_TOKEN
import com.fionpay.agent.utils.Constant.PREF_TOTAL_MODEM
import com.fionpay.agent.utils.Constant.PREF_TOTAL_PENDING
import com.fionpay.agent.utils.Constant.PREF_TOTAL_TRANSACTIONS
import com.fionpay.agent.utils.Constant.PREF_TOTAL_TRX_AMOUNT

/**
 * Akash.Singh
 * RootFicus.
 */
class SharedPreference(context: Context) {
    private val PREFS_NAME = "fion_modem_pref_file"
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
        return pref.getString(Constant.PREF_FULL_NAME,"Akash")
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

    fun setTotalPending(totalPending: Long) {
        pref.edit().putLong(PREF_TOTAL_PENDING,totalPending).apply()
    }

    fun getTotalPending(): Long {
        return pref.getLong(PREF_TOTAL_PENDING,0)
    }

    fun setTotalTransactions(totalTransactions: String) {
        pref.edit().putString(PREF_TOTAL_TRANSACTIONS,totalTransactions).apply()
    }

    fun getTotalTransactions(): String? {
        return pref.getString(PREF_TOTAL_TRANSACTIONS,"0.0")
    }

    fun setTodayTransactions(todayTransactions: Long) {
        pref.edit().putLong(PREF_TODAY_TRANSACTIONS,todayTransactions).apply()
    }

    fun getTodayTransactions(): Long {
        return pref.getLong(PREF_TODAY_TRANSACTIONS,0)
    }

    fun setTotalTrxAmount(totalTrxAmount: String) {
        pref.edit().putString(PREF_TOTAL_TRX_AMOUNT,totalTrxAmount).apply()
    }

    fun getTotalTrxAmount(): String? {
        return pref.getString(PREF_TOTAL_TRX_AMOUNT,"0.0")
    }

    fun setTodayTrxAmount(todayTrxAmount: String) {
        pref.edit().putString(PREF_TODAY_TRX_AMOUNT,todayTrxAmount).apply()
    }

    fun getTodayTrxAmount(): String? {
        return pref.getString(PREF_TODAY_TRX_AMOUNT,"0.0")
    }

    fun setTotalModem(totalModem: Int) {
        pref.edit().putInt(PREF_TOTAL_MODEM,totalModem).apply()
    }

    fun getTotalModem(): Int {
        return pref.getInt(PREF_TOTAL_MODEM,0)
    }

    fun setBLSuccess(success: Int?) {
        success?.let { pref.edit().putInt(PREF_BL_SUCCESS, it).apply() }
    }

    fun getBLSuccess():Int{
        return pref.getInt(PREF_BL_SUCCESS,0)
    }

    fun setBLPending(pending: Int?) {
        pending?.let { pref.edit().putInt(PREF_BL_PENDING, it).apply() }
    }

    fun getBLPending():Int{
        return pref.getInt(PREF_BL_PENDING,0)
    }

    fun setBLRejected(rejected: Int?) {
        rejected?.let { pref.edit().putInt(PREF_BL_REJECTED, it).apply() }
    }

    fun getBLRejected():Int{
        return pref.getInt(PREF_BL_REJECTED,0)
    }

    fun setBLApproved(approved: Int?) {
        approved?.let { pref.edit().putInt(PREF_BL_APPROVED, it).apply() }
    }

    fun getBLApproved():Int{
        return pref.getInt(PREF_BL_APPROVED,0)
    }

    fun setBLDanger(danger: Int?) {
        danger?.let { pref.edit().putInt(PREF_BL_DANGER, it).apply() }
    }

    fun getBLDanger():Int{
        return pref.getInt(PREF_BL_DANGER,0)
    }

}