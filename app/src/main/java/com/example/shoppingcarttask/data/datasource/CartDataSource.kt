package com.example.shoppingcarttask.data.datasource

import com.example.shoppingcarttask.domain.models.CartItem

interface CartDataSource {
    suspend fun getItems(): List<CartItem>
    suspend fun insertItem(item: CartItem)
    suspend fun deleteItem(itemId: String)
}