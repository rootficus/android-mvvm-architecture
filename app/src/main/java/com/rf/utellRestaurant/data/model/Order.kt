package com.rf.utellRestaurant.data.model

import java.io.Serializable

data class Order(
    val name: String,
    val orderNumber: String,
    val date: String,
    val email: String,
    val address: String,
    val mobile: String,
    val type: String,
    val status: String,
    val total: String,
    val comment: String,
    val menus: ArrayList<Menus>

) : Serializable

data class Menus(
    val itemName: String,
    val quantity: String,
    val price: String
) : Serializable