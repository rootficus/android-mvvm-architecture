package com.rf.macgyver.data.model.response

import java.io.Serializable

data class UserInfo(
    var id: String,
    var user_name: String,
    var email: String,
    var phone: String,
    var full_name: String,
    var pin_code: Int,
    var country: String,
    var parent_id: String,
    var company_id: String,
    var role_id: String
) : Serializable