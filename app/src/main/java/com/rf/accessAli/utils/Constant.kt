package com.rf.accessAli.utils

/**
 * Akash.Singh
 * RootFicus.
 */
object Constant {

    const val PREF_PUSH_TOKEN = "PushToken"

    const val PREF_USER_ID = "UserId"

    const val PREF_PHONE_NUMBER = "phoneNumber"

    const val PREF_FULL_NAME = "fullName"

    const val PREF_PIN_CODE = "pinCode"

    const val PREF_TOKEN = "token"

    const val PREFS_IS_LOGIN = "isLogin"

    const val PREF_PASSWORD = "password"

    const val PREF_SIGN_IN_MODEL = "signInModel"

    const val PREF_SIGN_IN_LIST = "signInList"

    const val CHANNEL_ID = "utell_restaurant_channel_id"

    const val CHANNEL_NAME = "Utell Restaurant Channel"

    const val EDIT_TEXT_6 = "editText6"

    const val EDIT_TEXT_7 = "editText7"

    const val EDIT_TEXT_8 = "editText8"

    const val EDIT_TEXT_10 = "editText10"

    const val EDIT2_TEXT_10 = "edit2Text10"

    const val EDIT_TEXT_14 = "editText14"

    const val EXPIRE_HOUR = "expireHour"

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
    ResponseCode422(422),
    ResponseCode404(404),
    ResponseCode500(500)
}