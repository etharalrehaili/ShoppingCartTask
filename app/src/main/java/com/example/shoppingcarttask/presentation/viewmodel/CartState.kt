package com.example.shoppingcarttask.presentation.viewmodel

import com.example.shoppingcarttask.domain.models.CartItem

sealed class CartState {
    object Loading : CartState()
    data class Success(val items: List<CartItem>) : CartState()
    data class Error(val message: String) : CartState()
}