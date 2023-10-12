package com.fionpay.agent.ui.main.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.FilterResponse
import com.fionpay.agent.data.model.request.GetBalanceByFilterRequest
import com.fionpay.agent.data.model.request.GetMessageByFilterRequest
import com.fionpay.agent.data.model.request.GetPendingModemRequest
import com.fionpay.agent.data.model.request.ModemItemModel
import com.fionpay.agent.data.model.request.ProfileResponse
import com.fionpay.agent.data.model.request.SignInRequest
import com.fionpay.agent.data.model.request.UpdateActiveInActiveRequest
import com.fionpay.agent.data.model.request.UpdateAvailabilityRequest
import com.fionpay.agent.data.model.request.UpdateBalanceRequest
import com.fionpay.agent.data.model.request.UpdateLoginRequest
import com.fionpay.agent.data.model.response.BLTransactionModemResponse
import com.fionpay.agent.data.model.response.DashBoardItemResponse
import com.fionpay.agent.data.model.response.GetAddModemBalanceResponse
import com.fionpay.agent.data.model.response.GetAddModemResponse
import com.fionpay.agent.data.model.response.GetBalanceManageRecord
import com.fionpay.agent.data.model.response.GetMessageManageRecord
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.data.model.response.GetStatusCountResponse
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.data.model.response.SignInResponse
import com.fionpay.agent.data.model.response.TransactionModel
import com.fionpay.agent.data.model.response.TransactionModemResponse
import com.fionpay.agent.data.repository.DashBoardRepository
import com.fionpay.agent.ui.base.BaseViewModel
import com.fionpay.agent.utils.ResponseData
import com.fionpay.agent.utils.setError
import com.fionpay.agent.utils.setLoading
import com.fionpay.agent.utils.setSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(private val dashBoardRepository: DashBoardRepository) :
    BaseViewModel() {


    enum class DarkModeConfig {
        YES,
        NO
    }

    val signInResponseModel = MutableLiveData<ResponseData<SignInResponse>>()
    fun signInNow(signInRequest: SignInRequest) {
        signInResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.signInNow({ success -> signInResponseModel.setSuccess(success) },
                { error -> signInResponseModel.setError(error) },
                signInRequest,
                { message -> signInResponseModel.setError(message) })
        }
    }

    val dashBoardItemResponseModel = MutableLiveData<ResponseData<DashBoardItemResponse>>()
    fun dashBoardData() {
        dashBoardItemResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.dashBoardData({ success ->
                dashBoardItemResponseModel.setSuccess(
                    success
                )
            },
                { error -> dashBoardItemResponseModel.setError(error) },
                { message -> dashBoardItemResponseModel.setError(message) })
        }
    }


    val blTransactionsDataResponseModel =
        MutableLiveData<ResponseData<ArrayList<BLTransactionModemResponse>>>()

    fun getBlTransactionsData() {
        blTransactionsDataResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getBlTransactionsData({ success ->
                blTransactionsDataResponseModel.setSuccess(
                    success
                )
            },
                { error -> blTransactionsDataResponseModel.setError(error) },
                { message -> blTransactionsDataResponseModel.setError(message) })
        }
    }

    val transactionsDataResponseModel =
        MutableLiveData<ResponseData<ArrayList<TransactionModemResponse>>>()

    fun getTransactionsData(getPendingModemRequest: GetPendingModemRequest) {
        transactionsDataResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getTransactionsData({ success ->
                transactionsDataResponseModel.setSuccess(
                    success
                )
            },
                getPendingModemRequest,
                { error -> transactionsDataResponseModel.setError(error) },
                { message -> transactionsDataResponseModel.setError(message) })
        }
    }

    val getPendingModemResponseModel =
        MutableLiveData<ResponseData<ArrayList<PendingModemResponse>>>()

    fun getPendingRequest(pendingModemRequest: GetPendingModemRequest) {
        getPendingModemResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getPendingRequest(
                pendingModemRequest,
                { success ->
                    getPendingModemResponseModel.setSuccess(
                        success
                    )
                },
                { error -> getPendingModemResponseModel.setError(error) },
                { message -> getPendingModemResponseModel.setError(message) })
        }
    }

    val generatePinCodeResponseModel =
        MutableLiveData<ResponseData<Any>>()

    fun generatePinCode() {
        generatePinCodeResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.generatePinCode(
                { success ->
                    generatePinCodeResponseModel.setSuccess(
                        success
                    )
                },
                { error -> generatePinCodeResponseModel.setError(error) },
                { message -> generatePinCodeResponseModel.setError(message) })
        }
    }

    val getAgentProfileResponseModel =
        MutableLiveData<ResponseData<ProfileResponse>>()

    fun getAgentProfile(agentId: String) {
        getAgentProfileResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getAgentProfile(
                agentId,
                { success ->
                    getAgentProfileResponseModel.setSuccess(
                        success
                    )
                },
                { error -> getAgentProfileResponseModel.setError(error) },
                { message -> getAgentProfileResponseModel.setError(message) })
        }
    }
    val getTransactionFiltersResponseModel =
        MutableLiveData<ResponseData<FilterResponse>>()

    fun getTransactionFilters() {
        getTransactionFiltersResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getTransactionFilters(
                { success ->
                    getTransactionFiltersResponseModel.setSuccess(
                        success
                    )
                },
                { error -> getTransactionFiltersResponseModel.setError(error) },
                { message -> getTransactionFiltersResponseModel.setError(message) })
        }
    }

    val updateAgentProfileResponseModel =
        MutableLiveData<ResponseData<ProfileResponse>>()

    fun updateAgentProfile(fullName: RequestBody, filePart: MultipartBody.Part, agentId: String) {
        updateAgentProfileResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.updateAgentProfile(
                agentId,
                { success ->
                    updateAgentProfileResponseModel.setSuccess(
                        success
                    )
                },
                fullName,
                filePart,
                { error -> updateAgentProfileResponseModel.setError(error) },
                { message -> updateAgentProfileResponseModel.setError(message) })
        }
    }


    val updateAgentWithoutProfileResponseModel =
        MutableLiveData<ResponseData<ProfileResponse>>()

    fun updateAgentWithoutProfile(fullName: RequestBody, agentId: String) {
        updateAgentWithoutProfileResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.updateAgentWithoutProfile(
                agentId,
                { success ->
                    updateAgentWithoutProfileResponseModel.setSuccess(
                        success
                    )
                },
                fullName,
                { error -> updateAgentWithoutProfileResponseModel.setError(error) },
                { message -> updateAgentWithoutProfileResponseModel.setError(message) })
        }
    }

    val getBalanceManageRecordModel = MutableLiveData<ResponseData<List<GetBalanceManageRecord>>>()
    fun getBalanceByFilter(getBalanceByFilterRequest: GetBalanceByFilterRequest) {
        getBalanceManageRecordModel.setLoading(null)

        if (dashBoardRepository.getCountBalanceManageRecord(-1) > 0) {
            var arrayList = getBalanceByFilterRequest.balance_manager_filter?.let {
                getBalanceManageRecord(it)
            }
            getBalanceManageRecordModel.setSuccess(arrayList)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                dashBoardRepository.getBalanceByFilter(
                    { success ->
                        GlobalScope.launch {
                            success.forEach {
                                dashBoardRepository.insertBalanceManagerRecord(it)
                            }
                        }
                        getBalanceManageRecordModel.setSuccess(success)
                    },
                    { error -> getBalanceManageRecordModel.setError(error) },
                    getBalanceByFilterRequest,
                    { message -> getBalanceManageRecordModel.setError(message) })
            }
        }

    }

    val updateBLStatusResponseModel = MutableLiveData<ResponseData<GetBalanceManageRecord>>()
    fun updateBLStatus(updateBalanceRequest: UpdateBalanceRequest) {
        updateBLStatusResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.updateBLStatus({ success ->
                updateBLStatusResponseModel.setSuccess(
                    success
                )
            },
                { error -> updateBLStatusResponseModel.setError(error) },
                updateBalanceRequest,
                { message -> updateBLStatusResponseModel.setError(message) })
        }
    }

    fun getBalanceManageRecord(it: Int) = dashBoardRepository.getBalanceManageRecord(it)

    fun getCountBalanceManageRecord(it: Int) = dashBoardRepository.getCountBalanceManageRecord(it)


    val getMessageManageRecordModel = MutableLiveData<ResponseData<List<GetMessageManageRecord>>>()
    fun getMessageByFilter(getMessageByFilterRequest: GetMessageByFilterRequest) {
        getMessageManageRecordModel.setLoading(null)

        if (dashBoardRepository.getCountMessageManageRecord(-1) > 0) {
            var arrayList = getMessageByFilterRequest.message_type?.let {
                getMessageManageRecord(it)
            }
            getMessageManageRecordModel.setSuccess(arrayList)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                dashBoardRepository.getMessageByFilter(
                    { success ->
                        GlobalScope.launch {
                            success.forEach {
                                dashBoardRepository.insertGetMessageManageRecord(it)
                            }
                        }
                        getMessageManageRecordModel.setSuccess(success)
                    },
                    { error -> getMessageManageRecordModel.setError(error) },
                    getMessageByFilterRequest,
                    { message -> getMessageManageRecordModel.setError(message) })
            }
        }
    }

    fun getMessageManageRecord(it: Int) = dashBoardRepository.getMessageManageRecord(it)

    fun getCountMessageManageRecord(it: Int) = dashBoardRepository.getCountMessageManageRecord(it)

    val getModemsListResponseModel = MutableLiveData<ResponseData<List<GetModemsListResponse>>>()
    fun getModemsList() {
        getModemsListResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getModemsList({ success ->
                getModemsListResponseModel.setSuccess(
                    success
                )
            },
                { error -> getModemsListResponseModel.setError(error) },
                { message -> getModemsListResponseModel.setError(message) })
        }
    }

    val getAddModemItemResponseModel = MutableLiveData<ResponseData<GetAddModemResponse>>()
    fun addModemItem(modemItemModel: ModemItemModel) {
        getAddModemItemResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.addModemItem({ success ->
                getAddModemItemResponseModel.setSuccess(
                    success
                )
            },
                { error -> getAddModemItemResponseModel.setError(error) },
                modemItemModel,
                { message -> getAddModemItemResponseModel.setError(message) })
        }
    }

    val getAddModemBalanceResponseModel =
        MutableLiveData<ResponseData<GetAddModemBalanceResponse>>()

    fun addModemBalance(addModemBalanceModel: AddModemBalanceModel) {
        getAddModemBalanceResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.addModemBalance({ success ->
                getAddModemBalanceResponseModel.setSuccess(
                    success
                )
            },
                { error -> getAddModemBalanceResponseModel.setError(error) },
                addModemBalanceModel,
                { message -> getAddModemBalanceResponseModel.setError(message) })
        }
    }

    val getStatusCountResponseModel = MutableLiveData<ResponseData<GetStatusCountResponse>>()
    fun getStatusCount() {
        getStatusCountResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getStatusCount({ success ->
                getStatusCountResponseModel.setSuccess(
                    success
                )
            },
                { error -> getStatusCountResponseModel.setError(error) },
                { message -> getStatusCountResponseModel.setError(message) })
        }
    }

    val getUpdateActiveInActiveStatusResponseModel = MutableLiveData<ResponseData<Any>>()
    fun updateActiveInActiveStatus(updateActiveInActiveRequest: UpdateActiveInActiveRequest) {
        getUpdateActiveInActiveStatusResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.updateActiveInActiveStatus({ success ->
                getUpdateActiveInActiveStatusResponseModel.setSuccess(
                    success
                )
            },
                { error -> getUpdateActiveInActiveStatusResponseModel.setError(error) },
                updateActiveInActiveRequest,
                { message -> getUpdateActiveInActiveStatusResponseModel.setError(message) })
        }
    }

    val getUpdateAvailabilityStatusResponseModel = MutableLiveData<ResponseData<Any>>()
    fun updateAvailabilityStatus(updateAvailabilityRequest: UpdateAvailabilityRequest) {
        getUpdateAvailabilityStatusResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.updateAvailabilityStatus({ success ->
                getUpdateAvailabilityStatusResponseModel.setSuccess(
                    success
                )
            },
                updateAvailabilityRequest,
                { error -> getUpdateAvailabilityStatusResponseModel.setError(error) },
                { message -> getUpdateAvailabilityStatusResponseModel.setError(message) })
        }
    }

    val getUpdateLoginStatusResponseModel = MutableLiveData<ResponseData<Any>>()
    fun updateLoginStatus(updateLoginRequest: UpdateLoginRequest) {
        getUpdateLoginStatusResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.updateLoginStatus({ success ->
                getUpdateLoginStatusResponseModel.setSuccess(
                    success
                )
            },
                updateLoginRequest,
                { error -> getUpdateLoginStatusResponseModel.setError(error) },
                { message -> getUpdateLoginStatusResponseModel.setError(message) })
        }
    }

    fun checkNightTheme(mode: Boolean) {
        if (mode) {
            shouldEnableDarkMode(DarkModeConfig.YES)
        } else {
            shouldEnableDarkMode(DarkModeConfig.NO)
        }

    }

    private fun shouldEnableDarkMode(config: DarkModeConfig) {
        when (config) {
            DarkModeConfig.YES -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            DarkModeConfig.NO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //delegate.applyDayNight()
    }

    fun setUserId(userId: String?) {
        dashBoardRepository.setUserId(userId)
    }

    fun setEmail(email: String?) {
        dashBoardRepository.setEmail(email)
    }

    fun setFullName(fullName: String?) {
        dashBoardRepository.setFullName(fullName)
    }

    fun getFullName(): String? {
        return dashBoardRepository.getFullName()
    }

    fun setProfileImage(fullName: String?) {
        dashBoardRepository.setProfileImage(fullName)
    }

    fun getProfileImage(): String? {
        return dashBoardRepository.getProfileImage()
    }

    fun setPinCode(pin_code: String?) {
        dashBoardRepository.setPinCode(pin_code)
    }

    fun setCountry(country: String?) {
        dashBoardRepository.setCountry(country)
    }

    fun setParentId(parent_id: String?) {
        dashBoardRepository.setParentId(parent_id)
    }

    fun setPhoneNumber(phone: String?) {
        dashBoardRepository.setPhoneNumber(phone)
    }

    fun setUserName(user_name: String?) {
        dashBoardRepository.setUserName(user_name)
    }

    fun setUserRole(role_id: String?) {
        dashBoardRepository.setUserRole(role_id)
    }

    fun isLogin(): Boolean {
        return dashBoardRepository.isLogin()
    }

    fun setToken(token: String) {
        dashBoardRepository.setToken(token)
    }

    fun setPassword(password: String) {
        dashBoardRepository.setPassword(password)
    }

    fun getEmail(): String? {
        return dashBoardRepository.getEmail()
    }

    fun getPassword(): String? {
        return dashBoardRepository.getPassword()
    }

    fun setIsLogin(isLogin: Boolean) {
        dashBoardRepository.setIsLogin(isLogin)
    }

    fun getUserId(): String? {
        return dashBoardRepository.getUserId()
    }

    fun getPinCode(): Int? {
        return dashBoardRepository.getPinCode()
    }

    fun setTotalPending(totalPending: Long) {
        dashBoardRepository.setTotalPending(totalPending)
    }

    fun getTotalPending(): Long {
        return dashBoardRepository.getTotalPending()
    }

    fun setTotalTransactions(totalTransactions: String) {
        dashBoardRepository.setTotalTransactions(totalTransactions)
    }

    fun getTotalTransactions(): String? {
        return dashBoardRepository.getTotalTransactions()
    }

    fun setTodayTransactions(todayTransactions: Long) {
        dashBoardRepository.setTodayTransactions(todayTransactions)
    }

    fun getTodayTransactions(): Long {
        return dashBoardRepository.getTodayTransactions()
    }

    fun setTotalTrxAmount(totalTrxAmount: String) {
        dashBoardRepository.setTotalTrxAmount(totalTrxAmount)
    }

    fun getTotalTrxAmount(): String? {
        return dashBoardRepository.getTotalTrxAmount()
    }

    fun setTodayTrxAmount(todayTrxAmount: String) {
        dashBoardRepository.setTodayTrxAmount(todayTrxAmount)
    }

    fun getTodayTrxAmount(): String? {
        return dashBoardRepository.getTodayTrxAmount()
    }

    fun setTotalModem(totalModem: Int) {
        dashBoardRepository.setTotalModem(totalModem)
    }

    fun getTotalModem(): Int {
        return dashBoardRepository.getTotalModem()
    }

    fun setBLSuccess(success: Int?) {
        dashBoardRepository.setBLSuccess(success)
    }

    fun getBLSuccess(): Int {
        return dashBoardRepository.getBLSuccess()
    }

    fun setBLPending(pending: Int?) {
        dashBoardRepository.setBLPending(pending)
    }

    fun getBLPending(): Int {
        return dashBoardRepository.getBLPending()
    }

    fun setBLRejected(rejected: Int?) {
        dashBoardRepository.setBLRejected(rejected)
    }

    fun getBLRejected(): Int {
        return dashBoardRepository.getBLRejected()
    }

    fun setBLApproved(approved: Int?) {
        dashBoardRepository.setBLApproved(approved)
    }

    fun getBLApproved(): Int {
        return dashBoardRepository.getBLApproved()
    }

    fun setDashBoardDataModel(model: String?) {
        dashBoardRepository.setDashBoardDataModel(model)
    }

    fun getDashBoardDataModel(): String? {
        return dashBoardRepository.getDashBoardDataModel()
    }

    fun setBLDanger(danger: Int?) {
        dashBoardRepository.setBLDanger(danger)
    }

    fun getBLDanger(): Int {
        return dashBoardRepository.getBLDanger()
    }

    fun deleteLocalBlManager() {
        dashBoardRepository.deleteLocalBlManager()
    }

    fun deleteLocalMessageManager() {
        dashBoardRepository.deleteLocalMessageManager()
    }

    fun updateLocalBalanceManager(balanceManageRecord: GetBalanceManageRecord) =
        dashBoardRepository.updateLocalBalanceManager(balanceManageRecord)

}