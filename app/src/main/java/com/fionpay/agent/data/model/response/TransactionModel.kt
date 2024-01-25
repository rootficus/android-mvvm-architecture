package com.fionpay.agent.data.model.response

data class TransactionModel(
    var title: String,
    var amount: Float?,
    val icon: Int,
)