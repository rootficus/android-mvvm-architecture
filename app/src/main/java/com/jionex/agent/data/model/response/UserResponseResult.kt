package com.jionex.agent.data.model.response

import com.jionex.agent.data.model.UserInfo

data class UserResponseResult (val data: UserInfo,val status: String, val message: String)