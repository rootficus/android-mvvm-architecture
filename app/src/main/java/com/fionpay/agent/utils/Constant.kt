package com.fionpay.agent.utils

/**
 * Akash.Singh
 * RootFicus.
 */
object Constant {

    const val bKASH_Commission_Percentage: Double = 0.41
    const val DATE_FORMAT_DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm"
    const val DATE_FORMAT_DD_MMMM_YY_hh_mm_ss_a = "dd-MMMM-yy hh:mm:ss a"
    const val PREF_SIM2_AGENT_BALANCE = "sim2AgentAmount"

    const val PREF_SIM1_AGENT_BALANCE = "sim1AgentAmount"

    const val ACTION_EVENT_SMS_SYNC = "com.akrobyte.rapidx.ActionEventSMSSync"
    const val ACTION_EVENT_BALANCE_SYNC = "com.akrobyte.rapidx.ActionEventBALANCESync"
    const val MESSAGE_ID = "messageId"

    const val PREF_PUSH_TOKEN = "PushToken"

    const val PREF_AUTH_TOKEN = "AuthToken"

    const val PREF_USER_ID = "UserId"

    const val PREF_USER_NAME = "UserName"

    const val PREF_EMAIL = "email"

    const val PREF_PHONE_NUMBER = "phoneNumber"

    const val PREF_FULL_NAME = "fullName"

    const val PREF_PIN_CODE = "pinCode"

    const val PREF_COUNTRY = "country";

    const val PREF_PARENT_ID = "ParentId"

    const val PREF_USER_ROLE = "userRoles"

    const val PREF_MODEM_SETUP = "ModemSetup"

    const val PREF_TOKEN = "token"

    const val PREFS_IS_LOGIN = "isLogin"

    const val PREF_PASSWORD = "password"

    const val PREF_TOTAL_PENDING = "totalPending"

    const val PREF_TOTAL_TRANSACTIONS = "totalTransactions"

    const val PREF_TODAY_TRANSACTIONS = "todayTransactions"

    const val PREF_TODAY_TRX_AMOUNT = "todayTrxAmount"

    const val PREF_TOTAL_TRX_AMOUNT = "totalTrxAmount"

    const val PREF_TOTAL_MODEM = "todayModem"

    const val PREF_BL_SUCCESS = "BlSuccess"

    const val PREF_BL_PENDING = "BlPending"

    const val PREF_BL_REJECTED = "BlRejected"

    const val PREF_BL_APPROVED = "BlApproved"

    const val PREF_BL_DANGER = "BlDanger"

    const val PREF_DASHBOARD = "DashBoardModel"

    enum class BalanceManagerStatus(val action: Int) {
        SUCCESS(0),
        PENDING(1),
        FAKE(2),
        REJECTED(3),
        APPROVED(4),
        DANGER(5),
        All(-1)
    }

    enum class SMSType(val value: Int) {
        CashIn(0),
        CashOut(1),
        B2B(2),
        UNKNOWN(3),
        All(-1)
    }

    enum class SyncData(val value: Int) {
        SyncPending(0),
        SyncSuccess(1),
        SyncFail(2),
        SyncInProgress(3)
    }


}


enum class APIResponseCode(val codeValue: Int) {
    ResponseCode100(100),
    ResponseCode101(101),
    ResponseCode104(104),
    ResponseCode200(200),
    ResponseCode201(201),
    ResponseCode400(400),
    ResponseCode401(401),
    ResponseCode403(403),
    ResponseCode404(404),
    ResponseCode500(500)
}