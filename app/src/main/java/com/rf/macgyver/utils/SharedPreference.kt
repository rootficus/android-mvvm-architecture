package com.rf.macgyver.utils

import android.content.Context
import com.rf.macgyver.utils.Constant.PREFS_IS_LOGIN
import com.rf.macgyver.utils.Constant.PREF_BL_APPROVED
import com.rf.macgyver.utils.Constant.PREF_BL_DANGER
import com.rf.macgyver.utils.Constant.PREF_BL_PENDING
import com.rf.macgyver.utils.Constant.PREF_BL_REJECTED
import com.rf.macgyver.utils.Constant.PREF_BL_SUCCESS
import com.rf.macgyver.utils.Constant.PREF_DASHBOARD
import com.rf.macgyver.utils.Constant.PREF_PASSWORD
import com.rf.macgyver.utils.Constant.PREF_PUSH_TOKEN
import com.rf.macgyver.utils.Constant.PREF_TOKEN

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

    fun setUserId(userId: String) {
        pref.edit().putString(Constant.PREF_USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return pref.getString(Constant.PREF_USER_ID, "")
    }

    fun setEmail(email: String) {
        pref.edit().putString(Constant.PREF_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return pref.getString(Constant.PREF_EMAIL, "")
    }

    fun getPassword(): String? {
        return pref.getString(PREF_PASSWORD, "")
    }

    fun setPhoneNumber(phoneNumber: String) {
        pref.edit().putString(Constant.PREF_PHONE_NUMBER, phoneNumber).apply()
    }

    fun setFullName(fullName: String) {
        pref.edit().putString(Constant.PREF_FULL_NAME, fullName).apply()
    }

    fun getFullName(): String? {
        return pref.getString(Constant.PREF_FULL_NAME, "Akash")
    }

    fun setProfileImage(image: String) {
        pref.edit().putString(Constant.PREF_IMAGE, image).apply()
    }

    fun getProfileImage(): String? {
        return pref.getString(Constant.PREF_IMAGE, "Akash")
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

    fun getPushToken(): String? {
        return pref.getString(
            PREF_PUSH_TOKEN, ""
        )
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

    fun setBLSuccess(success: Int?) {
        success?.let { pref.edit().putInt(PREF_BL_SUCCESS, it).apply() }
    }

    fun getBLSuccess(): Int {
        return pref.getInt(PREF_BL_SUCCESS, 0)
    }

    fun setBLPending(pending: Int?) {
        pending?.let { pref.edit().putInt(PREF_BL_PENDING, it).apply() }
    }

    fun getBLPending(): Int {
        return pref.getInt(PREF_BL_PENDING, 0)
    }

    fun setBLRejected(rejected: Int?) {
        rejected?.let { pref.edit().putInt(PREF_BL_REJECTED, it).apply() }
    }

    fun getBLRejected(): Int {
        return pref.getInt(PREF_BL_REJECTED, 0)
    }

    fun setBLApproved(approved: Int?) {
        approved?.let { pref.edit().putInt(PREF_BL_APPROVED, it).apply() }
    }

    fun getBLApproved(): Int {
        return pref.getInt(PREF_BL_APPROVED, 0)
    }

    fun setBLDanger(danger: Int?) {
        danger?.let { pref.edit().putInt(PREF_BL_DANGER, it).apply() }
    }

    fun getBLDanger(): Int {
        return pref.getInt(PREF_BL_DANGER, 0)
    }

    fun setDashBoardDataModel(model: String?) {
        model?.let { pref.edit().putString(PREF_DASHBOARD, it).apply() }
    }

    fun getDashBoardDataModel(): String? {
        return pref.getString(PREF_DASHBOARD, "")
    }

    fun setAvailableBalance(currentBalance: Float) {
        pref.edit().putFloat(Constant.PREF_AVAILABLE_BALANCE,currentBalance).apply()
    }

    fun getAvailableBalance(): Float {
        return pref.getFloat(Constant.PREF_AVAILABLE_BALANCE,0f)
    }

    fun setBalance(currentBalance: Float) {
        pref.edit().putFloat(Constant.PREF_BALANCE,currentBalance).apply()
    }

    fun getBalance(): Float {
        return pref.getFloat(Constant.PREF_BALANCE,0f)
    }

    fun setHoldBalance(currentBalance: Float) {
        pref.edit().putFloat(Constant.PREF_HOLD_BALANCE,currentBalance).apply()
    }

    fun getHoldBalance(): Float {
        return pref.getFloat(Constant.PREF_HOLD_BALANCE,0f)
    }


    fun resetSharedPref() {
        pref.edit().clear().apply()
    }

    fun setModemChangeStatusNotificationCount(count: Int) {
        pref.edit().putInt(Constant.PREF_MODEM_CHANGE_STATUS_NOTIFICATION_COUNT, count).apply()
    }

    fun getModemChangeStatusNotificationCount(): Int {
        return pref.getInt(Constant.PREF_MODEM_CHANGE_STATUS_NOTIFICATION_COUNT, 0)
    }

    fun setAddBalanceModemNotificationCount(count: Int) {
        pref.edit().putInt(Constant.PREF_ADD_BALANCE_MODEM_NOTIFICATION_COUNT, count).apply()
    }

    fun getAddBalanceModemNotificationCount(): Int {
        return pref.getInt(Constant.PREF_ADD_BALANCE_MODEM_NOTIFICATION_COUNT, 0)
    }

    fun setDepositRequestModemNotificationCount(count: Int) {
        pref.edit().putInt(Constant.PREF_DEPOSIT_REQUEST_MODEM_NOTIFICATION_COUNT, count).apply()
    }

    fun getDepositRequestModemNotificationCount(): Int {
        return pref.getInt(Constant.PREF_DEPOSIT_REQUEST_MODEM_NOTIFICATION_COUNT, 0)
    }

    fun setWithdrawalRequestModemNotificationCount(count: Int) {
        pref.edit().putInt(Constant.PREF_WITHDRAWAL_REQUEST_MODEM_NOTIFICATION_COUNT, count).apply()
    }

    fun getWithdrawalRequestModemNotificationCount(): Int {
        return pref.getInt(Constant.PREF_WITHDRAWAL_REQUEST_MODEM_NOTIFICATION_COUNT, 0)
    }

    fun setRefundRequestModemNotificationCount(count: Int) {
        pref.edit().putInt(Constant.PREF_REFUND_REQUEST_MODEM_NOTIFICATION_COUNT, count).apply()
    }

    fun getRefundRequestModemNotificationCount(): Int {
        return pref.getInt(Constant.PREF_REFUND_REQUEST_MODEM_NOTIFICATION_COUNT, 0)
    }

}