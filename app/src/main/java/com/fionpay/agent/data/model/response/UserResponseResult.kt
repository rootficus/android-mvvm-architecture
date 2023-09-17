package com.fionpay.agent.data.model.response

import com.fionpay.agent.data.model.UserInfo

data class UserResponseResult (val data: UserInfo,val status: String, val message: String)