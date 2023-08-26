package com.jionex.agent.data.repository

import android.content.Context
import android.util.Log
import com.jionex.agent.data.model.UserInfo
import com.jionex.agent.data.model.request.GetBalanceByFilterRequest
import com.jionex.agent.data.model.request.GetMessageByFilterRequest
import com.jionex.agent.data.model.request.GetModemsByFilterRequest
import com.jionex.agent.data.model.request.SignInRequest
import com.jionex.agent.data.model.response.DashBoardItemResponse
import com.jionex.agent.data.model.response.GetBalanceManageRecord
import com.jionex.agent.data.model.response.GetMessageByFilterResponse
import com.jionex.agent.data.model.response.GetModemsByFilterResponse
import com.jionex.agent.data.model.response.GetStatusCountResponse
import com.jionex.agent.data.model.response.SignInResponse
import com.jionex.agent.data.remote.JionexApiServices
import com.jionex.agent.roomDB.JionexDatabase
import com.jionex.agent.ui.base.BaseRepository
import com.jionex.agent.utils.Constant
import com.jionex.agent.utils.SharedPreference
import com.jionex.agent.utils.Utility

class DashBoardRepository (val apiServices: JionexApiServices,
                           val context: Context,
                           val sharedPreference: SharedPreference,
                           val jionexDatabase: JionexDatabase
) : BaseRepository() {


    fun checkOnVerificationAPI(
        success: (loginResponse: UserInfo) -> Unit,
        fail: (error: String) -> Unit,
        pinCode: String,
        message: (msg: String) -> Unit,
    ) {
        /*apiServices.verifyUserByPincode(pinCode).apply {
            execute(this, success, fail, context, message)
        }*/
    }

    fun signInNow(
        success: (signInResponse: SignInResponse) -> Unit,
        fail: (error: String) -> Unit,
        signInRequest: SignInRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.signInNow(signInRequest).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun dashBoardData(
        success: (dashBoardItemResponse : DashBoardItemResponse) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.dashboardData().apply {
            execute(this, success, fail, context, message)
        }
    }

    fun getBalanceByFilter(
        success: (getBalanceManageRecord: ArrayList<GetBalanceManageRecord>) -> Unit,
        fail: (error: String) -> Unit,
        getBalanceByFilterRequest: GetBalanceByFilterRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.getBalanceByFilter("Bearer " + sharedPreference.getToken()).apply {
            executeFilter(this, success, fail, context, message)
        }
    }
    fun getMessageByFilter(
        success: (getMessageByFilterResponse: List<GetMessageByFilterResponse>) -> Unit,
        fail: (error: String) -> Unit,
        getMessageByFilterRequest: GetMessageByFilterRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.getMessageByFilter("Bearer " + sharedPreference.getToken()).apply {
            executeFilter(this, success, fail, context, message)
        }
    }
    fun getModemsByFilter(
        success: (getMessageByFilterResponse: List<GetModemsByFilterResponse>) -> Unit,
        fail: (error: String) -> Unit,
        getModemsByFilterRequest: GetModemsByFilterRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.getModemsByFilter("Bearer " + sharedPreference.getToken(), getModemsByFilterRequest).apply {
            executeFilter(this, success, fail, context, message)
        }
    }
    fun getStatusCount(
        success: (getStatusCountResponse: GetStatusCountResponse) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getStatusCount("Bearer " + sharedPreference.getToken()).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun setUserId(userId: String?) {
        userId?.let { sharedPreference.setUserId(it) }
    }

    fun getUserId(): String? {
        return sharedPreference.getUserId()
    }

    fun setEmail(email: String?) {
        email?.let { sharedPreference.setEmail(it) }
    }

    fun setPassword(password:String?){
        password?.let { sharedPreference.setPassword(password) }
    }

    fun getPassword(): String? {
        return sharedPreference.getPassword()
    }

    fun setFullName(full_name: String?) {
        full_name?.let { sharedPreference.setFullName(it) }
    }

    fun setPinCode(pin_code: Int?) {
        pin_code?.let { sharedPreference.setPinCode(it) }
    }

    fun setCountry(country: String?) {
        country?.let { sharedPreference.setCountry(it) }
    }

    fun setParentId(parent_id: String?) {
        parent_id?.let { sharedPreference.setParentId(it) }
    }

    fun setPhoneNumber(phone: String?) {
        phone?.let { sharedPreference.setPhoneNumber(it) }
    }

    fun setUserName(user_name: String?) {
        user_name?.let { sharedPreference.setUserName(it) }
    }

    fun setUserRole(role_id: String?) {
        role_id?.let { sharedPreference.setUserRole(it) }
    }

    fun isLogin(): Boolean {
        return sharedPreference.isLogin()
    }

    fun setIsLogin(isLogin: Boolean?) {
        isLogin?.let { sharedPreference.setIsLogin(it) }
    }

    fun setToken(token: String?) {
        token?.let { sharedPreference.setToken(it) }
    }

    fun getFullName(): String? {
        return sharedPreference.getFullName()
    }

    fun getEmail():String?{
        return sharedPreference.getEmail()
    }

    fun getPinCode(): Int? {
        return sharedPreference.getPinCode()
    }

    fun setTotalPending(totalPending: Long) {
        sharedPreference.setTotalPending(totalPending)
    }

    fun getTotalPending(): Long {
        return sharedPreference.getTotalPending()
    }

    fun setTotalTransactions(totalTransactions: String) {
        sharedPreference.setTotalTransactions(totalTransactions)
    }

    fun getTotalTransactions(): String? {
        return sharedPreference.getTotalTransactions()
    }

    fun setTodayTransactions(todayTransactions: Long) {
        sharedPreference.setTodayTransactions(todayTransactions)
    }

    fun getTodayTransactions(): Long {
        return sharedPreference.getTodayTransactions()
    }

    fun setTotalTrxAmount(totalTrxAmount: String) {
        sharedPreference.setTotalTrxAmount(totalTrxAmount)
    }

    fun getTotalTrxAmount(): String? {
        return sharedPreference.getTotalTrxAmount()
    }

    fun setTodayTrxAmount(todayTrxAmount: String) {
        sharedPreference.setTodayTrxAmount(todayTrxAmount)
    }

    fun getTodayTrxAmount(): String? {
        return sharedPreference.getTodayTrxAmount()
    }

    fun setTotalModem(totalModem: Int) {
        sharedPreference.setTotalModem(totalModem)
    }

    fun getTotalModem(): Int {
        return sharedPreference.getTotalModem()
    }

    fun setBLSuccess(success: Int?) {
        sharedPreference.setBLSuccess(success)
    }

    fun getBLSuccess():Int{
        return sharedPreference.getBLSuccess()
    }

    fun setBLPending(pending: Int?) {
        sharedPreference.setBLPending(pending)
    }

    fun getBLPending():Int{
        return sharedPreference.getBLPending()
    }

    fun setBLRejected(rejected: Int?) {
        sharedPreference.setBLRejected(rejected)
    }

    fun getBLRejected():Int{
        return sharedPreference.getBLRejected()
    }

    fun setBLApproved(approved: Int?) {
        sharedPreference.setBLApproved(approved)
    }

    fun getBLApproved():Int{
        return sharedPreference.getBLApproved()
    }

    fun setBLDanger(danger: Int?) {
        sharedPreference.setBLDanger(danger)
    }

    fun getBLDanger():Int{
        return sharedPreference.getBLDanger()
    }

    fun insertBalanceManagerRecord(it: GetBalanceManageRecord) {
        jionexDatabase.rapidxDao()?.insertGetBalanceManageRecord(it)
    }

    fun getBalanceManageRecord(isBalanceManage : Int) : ArrayList<GetBalanceManageRecord> {
        return when(isBalanceManage){
            -1 -> jionexDatabase.rapidxDao()?.getBalanceTransaction() as ArrayList<GetBalanceManageRecord>
            Constant.BalanceManagerStatus.SUCCESS.action -> {
                Log.d("getBalanceManagerRecord","::${isBalanceManage},${Constant.BalanceManagerStatus.SUCCESS.toString()}")
                jionexDatabase.rapidxDao()?.getBalanceTransaction(Constant.BalanceManagerStatus.SUCCESS.toString()) as ArrayList<GetBalanceManageRecord>
            }
            Constant.BalanceManagerStatus.PENDING.action -> {
                Log.d("getBalanceManagerRecord","::${isBalanceManage},${Constant.BalanceManagerStatus.PENDING.toString()}")
                jionexDatabase.rapidxDao()?.getBalanceTransaction(Constant.BalanceManagerStatus.PENDING.toString()) as ArrayList<GetBalanceManageRecord>
            }
            Constant.BalanceManagerStatus.APPROVED.action -> {
                Log.d("getBalanceManagerRecord","::${isBalanceManage},${Constant.BalanceManagerStatus.APPROVED.toString()}")
                jionexDatabase.rapidxDao()?.getBalanceTransaction(Constant.BalanceManagerStatus.APPROVED.toString()) as ArrayList<GetBalanceManageRecord>
            }
            Constant.BalanceManagerStatus.REJECTED.action -> {
                Log.d("getBalanceManagerRecord","::${isBalanceManage},${Constant.BalanceManagerStatus.REJECTED.toString()}")
                jionexDatabase.rapidxDao()?.getBalanceTransaction(Constant.BalanceManagerStatus.REJECTED.toString()) as ArrayList<GetBalanceManageRecord>
            }
            Constant.BalanceManagerStatus.DANGER.action -> {
                Log.d("getBalanceManagerRecord","::${isBalanceManage},${Constant.BalanceManagerStatus.DANGER.toString()}")
                jionexDatabase.rapidxDao()?.getBalanceTransaction(Constant.BalanceManagerStatus.DANGER.toString()) as ArrayList<GetBalanceManageRecord>
            }
            else -> {
                Log.d("getBalanceManagerRecord",":else:${isBalanceManage},${Constant.BalanceManagerStatus.DANGER.toString()}")
                jionexDatabase.rapidxDao()?.getBalanceTransaction() as ArrayList<GetBalanceManageRecord>
            }
        }
    }

    fun getCountBalanceManageRecord(isBalanceManage : Int) : Int {
        return when(isBalanceManage){
            -1 -> jionexDatabase.rapidxDao()?.getCountBalanceTransaction() as Int
            Constant.BalanceManagerStatus.SUCCESS.action -> jionexDatabase.rapidxDao()?.getCountBalanceTransaction(Constant.BalanceManagerStatus.SUCCESS.toString()) as Int
            Constant.BalanceManagerStatus.PENDING.action -> jionexDatabase.rapidxDao()?.getCountBalanceTransaction(Constant.BalanceManagerStatus.PENDING.toString()) as Int
            Constant.BalanceManagerStatus.APPROVED.action -> jionexDatabase.rapidxDao()?.getCountBalanceTransaction(Constant.BalanceManagerStatus.APPROVED.toString()) as Int
            Constant.BalanceManagerStatus.REJECTED.action -> jionexDatabase.rapidxDao()?.getCountBalanceTransaction(Constant.BalanceManagerStatus.REJECTED.toString()) as Int
            Constant.BalanceManagerStatus.DANGER.action -> jionexDatabase.rapidxDao()?.getCountBalanceTransaction(Constant.BalanceManagerStatus.DANGER.toString()) as Int
            else -> {
                jionexDatabase.rapidxDao()?.getBalanceTransaction() as Int
            }
        }
    }

}