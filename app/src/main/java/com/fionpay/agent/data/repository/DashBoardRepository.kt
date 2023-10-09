package com.fionpay.agent.data.repository

import android.content.Context
import android.util.Log
import com.fionpay.agent.data.model.UserInfo
import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.GetBalanceByFilterRequest
import com.fionpay.agent.data.model.request.GetMessageByFilterRequest
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.request.SignInRequest
import com.fionpay.agent.data.model.request.UpdateActiveInActiveRequest
import com.fionpay.agent.data.model.request.UpdateAvailabilityRequest
import com.fionpay.agent.data.model.request.UpdateBalanceRequest
import com.fionpay.agent.data.model.request.UpdateLoginRequest
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.GetAddModemBalanceResponse
import com.fionpay.agent.data.model.response.GetAddModemResponse
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.data.model.response.GetStatusCountResponse
import com.fionpay.agent.data.model.response.SignInResponse
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.ui.base.BaseRepository
import com.fionpay.agent.utils.Constant
import com.fionpay.agent.utils.SharedPreference

class DashBoardRepository(
    val apiServices: FionApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val fionDatabase: FionDatabase
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
        success: (dashBoardItemResponse: DashBoardItemResponse) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.dashboardData().apply {
            execute(this, success, fail, context, message)
        }
    }

    fun getBlTransactionsData(
        success: (transactionModemList: ArrayList<TransactionModemResponse>) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getBlTransactionsData().apply {
            execute2(this, success, fail, context, message)
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

    fun updateBLStatus(
        success: (getBalanceManageRecord: GetBalanceManageRecord) -> Unit,
        fail: (error: String) -> Unit,
        updateBalanceRequest: UpdateBalanceRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.updateBLStatus("Bearer " + sharedPreference.getToken(), updateBalanceRequest)
            .apply {
                execute(this, success, fail, context, message)
            }
    }

    fun getMessageByFilter(
        success: (getMessageManageRecord: List<GetMessageManageRecord>) -> Unit,
        fail: (error: String) -> Unit,
        getMessageByFilterRequest: GetMessageByFilterRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.getMessageByFilter("Bearer " + sharedPreference.getToken()).apply {
            executeFilter(this, success, fail, context, message)
        }
    }

    fun getModemsList(
        success: (getMessageByFilterResponse: List<GetModemsListResponse>) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getModemsList("Bearer " + sharedPreference.getToken()).apply {
            executeFilter(this, success, fail, context, message)
        }
    }

    fun addModemItem(
        success: (getMessageByFilterResponse: GetAddModemResponse) -> Unit,
        fail: (error: String) -> Unit,
        modemItemModel: ModemItemModel,
        message: (msg: String) -> Unit
    ) {
        apiServices.addModemItem("Bearer " + sharedPreference.getToken(), modemItemModel).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun addModemBalance(
        success: (getAddModemBalanceResponse: GetAddModemBalanceResponse) -> Unit,
        fail: (error: String) -> Unit,
        addModemBalanceModel: AddModemBalanceModel,
        message: (msg: String) -> Unit
    ) {
        apiServices.addModemBalance("Bearer " + sharedPreference.getToken(), addModemBalanceModel)
            .apply {
                execute(this, success, fail, context, message)
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

    fun updateActiveInActiveStatus(
        success: (any: Any) -> Unit,
        fail: (error: String) -> Unit,
        updateActiveInActiveRequest: UpdateActiveInActiveRequest,
        message: (msg: String) -> Unit
    ) {
        apiServices.updateActiveInActiveStatus(
            "Bearer " + sharedPreference.getToken(),
            updateActiveInActiveRequest
        ).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun updateAvailabilityStatus(
        success: (any: Any) -> Unit,
        updateAvailabilityRequest: UpdateAvailabilityRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.updateAvailabilityStatus(
            "Bearer " + sharedPreference.getToken(),
            updateAvailabilityRequest
        ).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun updateLoginStatus(
        success: (any: Any) -> Unit,
        updateLoginRequest: UpdateLoginRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.updateLoginStatus("Bearer " + sharedPreference.getToken(), updateLoginRequest)
            .apply {
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

    fun setPassword(password: String?) {
        password?.let { sharedPreference.setPassword(password) }
    }

    fun getPassword(): String? {
        return sharedPreference.getPassword()
    }

    fun setFullName(full_name: String?) {
        full_name?.let { sharedPreference.setFullName(it) }
    }

    fun setPinCode(pin_code: String?) {
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

    fun getEmail(): String? {
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

    fun getBLSuccess(): Int {
        return sharedPreference.getBLSuccess()
    }

    fun setBLPending(pending: Int?) {
        sharedPreference.setBLPending(pending)
    }

    fun getBLPending(): Int {
        return sharedPreference.getBLPending()
    }

    fun setBLRejected(rejected: Int?) {
        sharedPreference.setBLRejected(rejected)
    }

    fun getBLRejected(): Int {
        return sharedPreference.getBLRejected()
    }

    fun setBLApproved(approved: Int?) {
        sharedPreference.setBLApproved(approved)
    }

    fun getBLApproved(): Int {
        return sharedPreference.getBLApproved()
    }

    fun setBLDanger(danger: Int?) {
        sharedPreference.setBLDanger(danger)
    }

    fun getBLDanger(): Int {
        return sharedPreference.getBLDanger()
    }

    fun setDashBoardDataModel(model: String?) {
        sharedPreference.setDashBoardDataModel(model)
    }

    fun getDashBoardDataModel(): String? {
        return sharedPreference.getDashBoardDataModel()
    }

    fun insertBalanceManagerRecord(it: GetBalanceManageRecord) {
        fionDatabase.rapidxDao()?.insertGetBalanceManageRecord(it)
    }

    fun getBalanceManageRecord(isBalanceManage: Int): ArrayList<GetBalanceManageRecord> {
        return when (isBalanceManage) {
            -1 -> fionDatabase.rapidxDao()
                ?.getBalanceTransaction() as ArrayList<GetBalanceManageRecord>

            Constant.BalanceManagerStatus.SUCCESS.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.SUCCESS.toString()}"
                )
                fionDatabase.rapidxDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.SUCCESS.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.PENDING.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.PENDING.toString()}"
                )
                fionDatabase.rapidxDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.PENDING.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.APPROVED.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.APPROVED.toString()}"
                )
                fionDatabase.rapidxDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.APPROVED.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.REJECTED.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.REJECTED.toString()}"
                )
                fionDatabase.rapidxDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.REJECTED.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.DANGER.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.DANGER.toString()}"
                )
                fionDatabase.rapidxDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.DANGER.toString()) as ArrayList<GetBalanceManageRecord>
            }

            else -> {
                Log.d(
                    "getBalanceManagerRecord",
                    ":else:${isBalanceManage},${Constant.BalanceManagerStatus.DANGER.toString()}"
                )
                fionDatabase.rapidxDao()
                    ?.getBalanceTransaction() as ArrayList<GetBalanceManageRecord>
            }
        }
    }

    fun getCountBalanceManageRecord(isBalanceManage: Int): Int {
        return when (isBalanceManage) {
            -1 -> fionDatabase.rapidxDao()?.getCountBalanceTransaction() as Int
            Constant.BalanceManagerStatus.SUCCESS.action -> fionDatabase.rapidxDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.SUCCESS.toString()) as Int

            Constant.BalanceManagerStatus.PENDING.action -> fionDatabase.rapidxDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.PENDING.toString()) as Int

            Constant.BalanceManagerStatus.APPROVED.action -> fionDatabase.rapidxDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.APPROVED.toString()) as Int

            Constant.BalanceManagerStatus.REJECTED.action -> fionDatabase.rapidxDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.REJECTED.toString()) as Int

            Constant.BalanceManagerStatus.DANGER.action -> fionDatabase.rapidxDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.DANGER.toString()) as Int

            else -> {
                fionDatabase.rapidxDao()?.getCountBalanceTransaction() as Int
            }
        }
    }

    fun insertGetMessageManageRecord(it: GetMessageManageRecord) {
        fionDatabase.rapidxDao()?.insertGetMessageManageRecord(it)
    }

    fun getMessageManageRecord(isBalanceManage: Int): ArrayList<GetMessageManageRecord> {
        return when (isBalanceManage) {
            Log.d("getMessageManageRecord", ":All:${isBalanceManage},${Constant.SMSType.All.value}")
                    - 1 -> fionDatabase.rapidxDao()
                ?.getMessageTransaction() as ArrayList<GetMessageManageRecord>

            Constant.SMSType.CashOut.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":CashOut:${isBalanceManage},${Constant.SMSType.CashOut.value}"
                )
                fionDatabase.rapidxDao()
                    ?.getMessageTransaction(Constant.SMSType.CashOut.value) as ArrayList<GetMessageManageRecord>
            }

            Constant.SMSType.CashIn.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":CashIn:${isBalanceManage},${Constant.SMSType.CashIn.value}"
                )
                fionDatabase.rapidxDao()
                    ?.getMessageTransaction(Constant.SMSType.CashIn.value) as ArrayList<GetMessageManageRecord>
            }

            Constant.SMSType.B2B.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":B2B:${isBalanceManage},${Constant.SMSType.B2B.value}"
                )
                fionDatabase.rapidxDao()
                    ?.getMessageTransaction(Constant.SMSType.B2B.value) as ArrayList<GetMessageManageRecord>
            }

            Constant.SMSType.UNKNOWN.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":UNKNOWN:${isBalanceManage},${Constant.SMSType.UNKNOWN.value}"
                )
                fionDatabase.rapidxDao()
                    ?.getMessageTransaction(Constant.SMSType.UNKNOWN.value) as ArrayList<GetMessageManageRecord>
            }

            else -> {
                Log.d(
                    "getMessageManageRecord",
                    ":AllElse:${isBalanceManage},${Constant.SMSType.All.value}"
                )
                fionDatabase.rapidxDao()
                    ?.getMessageTransaction() as ArrayList<GetMessageManageRecord>
            }
        }
    }

    fun getCountMessageManageRecord(isBalanceManage: Int): Int {
        return when (isBalanceManage) {
            -1 -> fionDatabase.rapidxDao()?.getCountMessageTransaction() as Int
            Constant.SMSType.CashOut.value -> fionDatabase.rapidxDao()
                ?.getCountMessageTransaction(Constant.SMSType.CashOut.value) as Int

            Constant.SMSType.CashIn.value -> fionDatabase.rapidxDao()
                ?.getCountMessageTransaction(Constant.SMSType.CashIn.value) as Int

            Constant.SMSType.B2B.value -> fionDatabase.rapidxDao()
                ?.getCountMessageTransaction(Constant.SMSType.B2B.value) as Int

            Constant.SMSType.UNKNOWN.value -> fionDatabase.rapidxDao()
                ?.getCountMessageTransaction(Constant.SMSType.UNKNOWN.value) as Int

            else -> {
                fionDatabase.rapidxDao()?.getCountMessageTransaction() as Int
            }
        }
    }

    fun deleteLocalBlManager() {
        fionDatabase.rapidxDao()?.deleteGetBalanceManageRecord()
    }

    fun deleteLocalMessageManager() {
        fionDatabase.rapidxDao()?.deleteGetMessageManageRecord()
    }

    fun updateLocalBalanceManager(balanceManageRecord: GetBalanceManageRecord) {
        fionDatabase.rapidxDao()?.updateLocalBalanceManager(balanceManageRecord)
    }

}