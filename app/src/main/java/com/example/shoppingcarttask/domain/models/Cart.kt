package com.example.shoppingcarttask.domain.models

data class Cart(
    val id: Int,
    val itemName: String,
    val price: Double,
    val quantity: Int,
)