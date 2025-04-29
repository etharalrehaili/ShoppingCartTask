package com.example.shoppingcarttask.domain.models

data class CartItem(
    val id: String,
    val name: String,
    val quantity: Int,
    val price: Double
)
