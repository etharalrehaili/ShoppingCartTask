package com.example.shoppingcarttask.domain.usecases

import com.example.shoppingcarttask.domain.models.Cart
import javax.inject.Inject

class AddItemToCartUseCase @Inject constructor() {
    operator fun invoke(cart: List<Cart>, newItem: Cart): List<Cart> {
        return cart + newItem
    }
}
