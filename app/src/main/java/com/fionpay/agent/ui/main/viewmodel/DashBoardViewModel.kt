package com.fionpay.agent.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fionpay.agent.data.model.request.AddModemBalanceModel
import com.fionpay.agent.data.model.request.AddModemSlotsModel
import com.fionpay.agent.data.model.request.Bank
import com.fionpay.agent.data.model.request.CheckNumberAvailabilityRequest
import com.fionpay.agent.data.model.request.CreateSupportRequest
import com.fionpay.agent.data.model.request.FilterResponse
import com.fionpay.agent.data.model.request.GetAgentB2BRequest
import com.fionpay.agent.data.model.request.GetBalanceByFilterRequest
import com.fionpay.agent.data.model.request.GetMessageByFilterRequest
import com.fionpay.agent.data.model.request.GetPendingModemRequest
import com.fionpay.agent.data.model.request.GetSupportRequest
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
import com.fionpay.agent.data.model.response.GetModemSlotsResponse
import com.fionpay.agent.data.model.response.GetModemsListResponse
import com.fionpay.agent.data.model.response.GetStatusCountResponse
import com.fionpay.agent.data.model.response.ModemPinCodeResponse
import com.fionpay.agent.data.model.response.PendingModemResponse
import com.fionpay.agent.data.model.response.ReturnBalanceResponse
import com.fionpay.agent.data.model.response.SupportResponse
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

    fun getTransactionsData(transactionFilterRequest: TransactionFilterRequest) {
        transactionsDataResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getTransactionsData({ success ->
                transactionsDataResponseModel.setSuccess(
                    success
                )
            },
                transactionFilterRequest,
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

    val checkNumberBankAvailabilityResponseModel =
        MutableLiveData<ResponseData<Any>>()

    fun checkNumberBankAvailability(checkNumberAvailabilityRequest: CheckNumberAvailabilityRequest) {
        checkNumberBankAvailabilityResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.checkNumberBankAvailability(
                { success ->
                    checkNumberBankAvailabilityResponseModel.setSuccess(
                        success
                    )
                },
                checkNumberAvailabilityRequest,
                { error -> checkNumberBankAvailabilityResponseModel.setError(error) },
                { message -> checkNumberBankAvailabilityResponseModel.setError(message) })
        }
    }

    val getBalanceManageRecordModel = MutableLiveData<ResponseData<List<GetBalanceManageRecord>>>()
    fun getBalanceByFilter(getBalanceByFilterRequest: GetBalanceByFilterRequest) {
        getBalanceManageRecordModel.setLoading(null)

        if (dashBoardRepository.getCountBalanceManageRecord(-1) > 0) {
            val arrayList = getBalanceByFilterRequest.balance_manager_filter?.let {
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

    fun insertModems(modem: Modem) {
        dashBoardRepository.insertModems(modem)
    }

    fun insertBanks(bank: Bank) {
        dashBoardRepository.insertBanks(bank)
    }

    fun getModemsListDao(): List<Modem>? {
        return dashBoardRepository.getModemsListDao()
    }

    fun getBanksListDao(): List<Bank>? {
        return dashBoardRepository.getBanksListDao()
    }


    val getMessageManageRecordModel = MutableLiveData<ResponseData<List<GetMessageManageRecord>>>()
    fun getMessageByFilter(getMessageByFilterRequest: GetMessageByFilterRequest) {
        getMessageManageRecordModel.setLoading(null)

        if (dashBoardRepository.getCountMessageManageRecord(-1) > 0) {
            val arrayList = getMessageByFilterRequest.message_type?.let {
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
                    { error -> getMessageManageRecordModel.setError(error) }
                ) { message -> getMessageManageRecordModel.setError(message) }
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

    val getModemSlotsResponseModel = MutableLiveData<ResponseData<List<GetModemSlotsResponse>>>()
    fun addModemSlots(addModemSlotsModel: AddModemSlotsModel) {
        getModemSlotsResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.addModemSlots({ success ->
                getModemSlotsResponseModel.setSuccess(
                    success
                )
            },
                { error -> getModemSlotsResponseModel.setError(error) },
                addModemSlotsModel,
                { message -> getModemSlotsResponseModel.setError(message) })
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

    val getB2BRecordResponseModel = MutableLiveData<ResponseData<List<B2BResponse>>>()
    fun getB2BRecord(getAgentB2BRequest: GetAgentB2BRequest) {
        getB2BRecordResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getB2BRecord({ success ->
                getB2BRecordResponseModel.setSuccess(
                    success
                )
            },
                getAgentB2BRequest,
                { error -> getB2BRecordResponseModel.setError(error) },
                { message -> getB2BRecordResponseModel.setError(message) })
        }
    }

    val getSupportDataResponseModel = MutableLiveData<ResponseData<List<SupportResponse>>>()
    fun getSupportData(getSupportRequest: GetSupportRequest) {
        getSupportDataResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getSupportData({ success ->
                getSupportDataResponseModel.setSuccess(
                    success
                )
            },
                getSupportRequest,
                { error -> getSupportDataResponseModel.setError(error) },
                { message -> getSupportDataResponseModel.setError(message) })
        }
    }
    val createSupportDataResponseModel = MutableLiveData<ResponseData<SupportResponse>>()
    fun createSupportTicket(createSupportRequest: CreateSupportRequest) {
        createSupportDataResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.createSupportTicket({ success ->
                createSupportDataResponseModel.setSuccess(
                    success
                )
            },
                createSupportRequest,
                { error -> createSupportDataResponseModel.setError(error) },
                { message -> createSupportDataResponseModel.setError(message) })
        }
    }

    val memberSupportDataResponseModel = MutableLiveData<ResponseData<SupportResponse>>()
    fun memberSupportTicket(createSupportRequest: CreateSupportRequest) {
        memberSupportDataResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.memberSupportTicket({ success ->
                memberSupportDataResponseModel.setSuccess(
                    success
                )
            },
                createSupportRequest,
                { error -> memberSupportDataResponseModel.setError(error) },
                { message -> memberSupportDataResponseModel.setError(message) })
        }
    }

    val getModemPinCodesResponseModel = MutableLiveData<ResponseData<List<ModemPinCodeResponse>>>()
    fun getModemPinCodes() {
        getModemPinCodesResponseModel.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.getModemPinCodes({ success ->
                getModemPinCodesResponseModel.setSuccess(
                    success
                )
            },
                { error -> getModemPinCodesResponseModel.setError(error) },
                { message -> getModemPinCodesResponseModel.setError(message) })
        }
    }

    val returnBalanceToDistributorResponseModem =
        MutableLiveData<ResponseData<ReturnBalanceResponse>>()

    fun returnBalanceToDistributor(returnBalanceRequest: ReturnBalanceRequest) {
        returnBalanceToDistributorResponseModem.setLoading(null)
        viewModelScope.launch(Dispatchers.IO) {
            dashBoardRepository.returnBalanceToDistributor(
                { success ->
                    returnBalanceToDistributorResponseModem.setSuccess(
                        success
                    )
                },
                returnBalanceRequest,
                { error -> returnBalanceToDistributorResponseModem.setError(error) },
                { message -> returnBalanceToDistributorResponseModem.setError(message) })
        }
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

    fun getEmail(): String? {
        return dashBoardRepository.getEmail()
    }

    fun getUserId(): String? {
        return dashBoardRepository.getUserId()
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

    fun setBalance(balance: Float) {
        return dashBoardRepository.setBalance(balance)
    }

    fun getBalance(): Float {
        return dashBoardRepository.getBalance()
    }

    fun setAvailableBalance(balance: Float) {
        return dashBoardRepository.setAvailableBalance(balance)
    }

    fun getAvailableBalance(): Float {
        return dashBoardRepository.getAvailableBalance()
    }

    fun setHoldBalance(balance: Float) {
        return dashBoardRepository.setHoldBalance(balance)
    }

    fun getHoldBalance(): Float {
        return dashBoardRepository.getHoldBalance()
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