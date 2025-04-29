package com.example.shoppingcarttask.presentation.viewmodel

import com.example.shoppingcarttask.domain.models.Cart

data class CartState(
    val loading: Boolean = false,
    val cart: List<Cart> = emptyList(),
    val error: String? = null,
)