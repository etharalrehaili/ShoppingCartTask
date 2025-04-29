package com.example.shoppingcarttask.data.repository

import com.example.shoppingcarttask.domain.models.Cart
import kotlinx.coroutines.delay
import javax.inject.Inject

class CartRepository @Inject constructor(){

    suspend fun getCart(): List<Cart> {
        delay(2000)
        return listOf(
            Cart(1, "T-Shirt", 79.99, 2),
            Cart(2, "Pants", 99.99, 1),
        )
    }
}