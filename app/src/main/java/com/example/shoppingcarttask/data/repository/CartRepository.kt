package com.example.shoppingcarttask.data.repository

import com.example.shoppingcarttask.domain.models.CartItem

interface CartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun addItem(item: CartItem)
    suspend fun removeItem(itemId: String)
}