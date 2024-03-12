package com.rf.geolgy.data.model

import java.io.Serializable

data class User(
    val name: String? = null,
    val password: String? = null,
) : Serializable