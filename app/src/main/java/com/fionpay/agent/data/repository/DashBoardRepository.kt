package com.fionpay.agent.data.repository

import android.content.Context
import android.util.Log
import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.CheckNumberAvailabilityRequest
import com.fionpay.agent.data.model.request.FilterResponse
import com.fionpay.agent.data.model.request.GetAgentB2BRequest
import com.fionpay.agent.data.model.request.GetBalanceByFilterRequest
import com.fionpay.agent.data.model.request.GetPendingModemRequest
import com.fionpay.agent.data.model.request.Modem
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.request.ProfileResponse
import com.fionpay.agent.data.model.request.ReturnBalanceRequest
import com.fionpay.agent.data.model.request.TransactionFilterRequest
import com.fionpay.agent.data.model.request.UpdateActiveInActiveRequest
import com.fionpay.agent.data.model.request.UpdateAvailabilityRequest
import com.fionpay.agent.data.model.request.UpdateBalanceRequest
import com.fionpay.agent.data.model.request.UpdateLoginRequest
import com.fionpay.agent.data.model.response.B2BResponse
import com.fionpay.agent.data.model.response.BLTransactionModemResponse
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.GetAddModemBalanceResponse
import com.fionpay.agent.data.model.response.GetAddModemResponse
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.data.model.response.GetStatusCountResponse
import com.fionpay.agent.data.model.response.ModemPinCodeResponse
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.data.model.response.ReturnBalanceResponse
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.data.remote.FionApiServices
import com.fionpay.agent.roomDB.FionDatabase
import com.fionpay.agent.ui.base.BaseRepository
import com.fionpay.agent.utils.Constant
import com.fionpay.agent.utils.SharedPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DashBoardRepository(
    val apiServices: FionApiServices,
    val context: Context,
    val sharedPreference: SharedPreference,
    val fionDatabase: FionDatabase
) : BaseRepository() {


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
        success: (transactionModemList: ArrayList<BLTransactionModemResponse>) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getBlTransactionsData().apply {
            execute2(this, success, fail, context, message)
        }
    }

    fun getTransactionsData(
        success: (transactionModemList: ArrayList<TransactionModemResponse>) -> Unit,
        transactionFilterRequest: TransactionFilterRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getTransactionsData(
            "Bearer " + sharedPreference.getToken(),
            transactionFilterRequest
        ).apply {
            execute2(this, success, fail, context, message)
        }
    }

    fun getPendingRequest(
        pendingModemRequest: GetPendingModemRequest,
        success: (transactionModemList: ArrayList<PendingModemResponse>) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getPendingRequest("Bearer " + sharedPreference.getToken(), pendingModemRequest)
            .apply {
                execute2(this, success, fail, context, message)
            }
    }

    fun generatePinCode(
        success: (pinCode: Any) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.generatePinCode("Bearer " + sharedPreference.getToken())
            .apply {
                execute(this, success, fail, context, message)
            }
    }

    fun getAgentProfile(
        agentId: String,
        success: (profileResponse: ProfileResponse) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getAgentProfile("Bearer " + sharedPreference.getToken(), agentId)
            .apply {
                execute(this, success, fail, context, message)
            }
    }

    fun getTransactionFilters(
        success: (filterResponse: FilterResponse) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getTransactionFilters("Bearer " + sharedPreference.getToken())
            .apply {
                execute(this, success, fail, context, message)
            }
    }

    fun updateAgentProfile(
        agentId: String,
        success: (profileResponse: ProfileResponse) -> Unit,
        fullName: RequestBody,
        filePart: MultipartBody.Part,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.updateAgentProfile(
            "Bearer " + sharedPreference.getToken(),
            fullName,
            filePart,
            agentId
        )
            .apply {
                execute(this, success, fail, context, message)
            }
    }

    fun updateAgentWithoutProfile(
        agentId: String,
        success: (profileResponse: ProfileResponse) -> Unit,
        fullName: RequestBody,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.updateAgentWithoutProfile(
            "Bearer " + sharedPreference.getToken(),
            fullName,
            agentId
        )
            .apply {
                execute(this, success, fail, context, message)
            }
    }

    fun checkNumberBankAvailability(
        success: (any: Any) -> Unit,
        checkNumberAvailabilityRequest: CheckNumberAvailabilityRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.checkNumberBankAvailability(
            "Bearer " + sharedPreference.getToken(),
            checkNumberAvailabilityRequest
        )
            .apply {
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

    fun getB2BRecord(
        success: (b2BResponse: List<B2BResponse>) -> Unit,
        getAgentB2BRequest: GetAgentB2BRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getB2BRecord("Bearer " + sharedPreference.getToken(), getAgentB2BRequest)
            .apply {
                execute2(this, success, fail, context, message)
            }
    }

    fun getModemPinCodes(
        success: (b2BResponse: List<ModemPinCodeResponse>) -> Unit,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.getModemPinCodes("Bearer " + sharedPreference.getToken()).apply {
            execute2(this, success, fail, context, message)
        }
    }

    fun returnBalanceToDistributor(
        success: (returnBalanceResponse: ReturnBalanceResponse) -> Unit,
        returnBalanceRequest: ReturnBalanceRequest,
        fail: (error: String) -> Unit,
        message: (msg: String) -> Unit
    ) {
        apiServices.returnBalanceToDistributor(
            "Bearer " + sharedPreference.getToken(),
            returnBalanceRequest
        ).apply {
            execute(this, success, fail, context, message)
        }
    }

    fun getUserId(): String? {
        return sharedPreference.getUserId()
    }

    fun setFullName(fullName: String?) {
        fullName?.let { sharedPreference.setFullName(it) }
    }

    fun getProfileImage(): String? {
        return sharedPreference.getProfileImage()
    }

    fun setProfileImage(image: String?) {
        image?.let { sharedPreference.setProfileImage(it) }
    }

    fun getFullName(): String? {
        return sharedPreference.getFullName()
    }

    fun getEmail(): String? {
        return sharedPreference.getEmail()
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

    fun setCurrentAgentBalance(balance: String) {
        sharedPreference.setCurrentAgentBalance(balance)
    }

    fun getCurrentAgentBalance(): String? {
        return sharedPreference.getCurrentAgentBalance()
    }

    fun insertBalanceManagerRecord(it: GetBalanceManageRecord) {
        fionDatabase.fioDao()?.insertGetBalanceManageRecord(it)
    }

    fun insertModems(it: Modem) {
        fionDatabase.fioDao()?.insertModems(it)
    }

    fun insertBanks(it: Bank) {
        fionDatabase.fioDao()?.insertBanks(it)
    }

    fun getModemsListDao(): List<Modem>? {
        return fionDatabase.fioDao()?.getModemsList()
    }

    fun getBanksListDao(): List<Bank>? {
        return fionDatabase.fioDao()?.getBanksList()
    }

    fun getBalanceManageRecord(isBalanceManage: Int): ArrayList<GetBalanceManageRecord> {
        return when (isBalanceManage) {
            -1 -> fionDatabase.fioDao()
                ?.getBalanceTransaction() as ArrayList<GetBalanceManageRecord>

            Constant.BalanceManagerStatus.SUCCESS.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.SUCCESS}"
                )
                fionDatabase.fioDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.SUCCESS.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.PENDING.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.PENDING}"
                )
                fionDatabase.fioDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.PENDING.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.APPROVED.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.APPROVED}"
                )
                fionDatabase.fioDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.APPROVED.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.REJECTED.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.REJECTED}"
                )
                fionDatabase.fioDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.REJECTED.toString()) as ArrayList<GetBalanceManageRecord>
            }

            Constant.BalanceManagerStatus.DANGER.action -> {
                Log.d(
                    "getBalanceManagerRecord",
                    "::${isBalanceManage},${Constant.BalanceManagerStatus.DANGER}"
                )
                fionDatabase.fioDao()
                    ?.getBalanceTransaction(Constant.BalanceManagerStatus.DANGER.toString()) as ArrayList<GetBalanceManageRecord>
            }

            else -> {
                Log.d(
                    "getBalanceManagerRecord",
                    ":else:${isBalanceManage},${Constant.BalanceManagerStatus.DANGER}"
                )
                fionDatabase.fioDao()
                    ?.getBalanceTransaction() as ArrayList<GetBalanceManageRecord>
            }
        }
    }

    fun getCountBalanceManageRecord(isBalanceManage: Int): Int {
        return when (isBalanceManage) {
            -1 -> fionDatabase.fioDao()?.getCountBalanceTransaction() as Int
            Constant.BalanceManagerStatus.SUCCESS.action -> fionDatabase.fioDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.SUCCESS.toString()) as Int

            Constant.BalanceManagerStatus.PENDING.action -> fionDatabase.fioDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.PENDING.toString()) as Int

            Constant.BalanceManagerStatus.APPROVED.action -> fionDatabase.fioDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.APPROVED.toString()) as Int

            Constant.BalanceManagerStatus.REJECTED.action -> fionDatabase.fioDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.REJECTED.toString()) as Int

            Constant.BalanceManagerStatus.DANGER.action -> fionDatabase.fioDao()
                ?.getCountBalanceTransaction(Constant.BalanceManagerStatus.DANGER.toString()) as Int

            else -> {
                fionDatabase.fioDao()?.getCountBalanceTransaction() as Int
            }
        }
    }

    fun insertGetMessageManageRecord(it: GetMessageManageRecord) {
        fionDatabase.fioDao()?.insertGetMessageManageRecord(it)
    }

    fun getMessageManageRecord(isBalanceManage: Int): ArrayList<GetMessageManageRecord> {
        return when (isBalanceManage) {
            Log.d("getMessageManageRecord", ":All:${isBalanceManage},${Constant.SMSType.All.value}")
                    - 1 -> fionDatabase.fioDao()
                ?.getMessageTransaction() as ArrayList<GetMessageManageRecord>

            Constant.SMSType.CashOut.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":CashOut:${isBalanceManage},${Constant.SMSType.CashOut.value}"
                )
                fionDatabase.fioDao()
                    ?.getMessageTransaction(Constant.SMSType.CashOut.value) as ArrayList<GetMessageManageRecord>
            }

            Constant.SMSType.CashIn.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":CashIn:${isBalanceManage},${Constant.SMSType.CashIn.value}"
                )
                fionDatabase.fioDao()
                    ?.getMessageTransaction(Constant.SMSType.CashIn.value) as ArrayList<GetMessageManageRecord>
            }

            Constant.SMSType.B2B.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":B2B:${isBalanceManage},${Constant.SMSType.B2B.value}"
                )
                fionDatabase.fioDao()
                    ?.getMessageTransaction(Constant.SMSType.B2B.value) as ArrayList<GetMessageManageRecord>
            }

            Constant.SMSType.UNKNOWN.value -> {
                Log.d(
                    "getMessageManageRecord",
                    ":UNKNOWN:${isBalanceManage},${Constant.SMSType.UNKNOWN.value}"
                )
                fionDatabase.fioDao()
                    ?.getMessageTransaction(Constant.SMSType.UNKNOWN.value) as ArrayList<GetMessageManageRecord>
            }

            else -> {
                Log.d(
                    "getMessageManageRecord",
                    ":AllElse:${isBalanceManage},${Constant.SMSType.All.value}"
                )
                fionDatabase.fioDao()
                    ?.getMessageTransaction() as ArrayList<GetMessageManageRecord>
            }
        }
    }

    fun getCountMessageManageRecord(isBalanceManage: Int): Int {
        return when (isBalanceManage) {
            -1 -> fionDatabase.fioDao()?.getCountMessageTransaction() as Int
            Constant.SMSType.CashOut.value -> fionDatabase.fioDao()
                ?.getCountMessageTransaction(Constant.SMSType.CashOut.value) as Int

            Constant.SMSType.CashIn.value -> fionDatabase.fioDao()
                ?.getCountMessageTransaction(Constant.SMSType.CashIn.value) as Int

            Constant.SMSType.B2B.value -> fionDatabase.fioDao()
                ?.getCountMessageTransaction(Constant.SMSType.B2B.value) as Int

            Constant.SMSType.UNKNOWN.value -> fionDatabase.fioDao()
                ?.getCountMessageTransaction(Constant.SMSType.UNKNOWN.value) as Int

            else -> {
                fionDatabase.fioDao()?.getCountMessageTransaction() as Int
            }
        }
    }

    fun deleteLocalBlManager() {
        fionDatabase.fioDao()?.deleteGetBalanceManageRecord()
    }

    fun deleteLocalMessageManager() {
        fionDatabase.fioDao()?.deleteGetMessageManageRecord()
    }

    fun updateLocalBalanceManager(balanceManageRecord: GetBalanceManageRecord) {
        fionDatabase.fioDao()?.updateLocalBalanceManager(balanceManageRecord)
    }

}