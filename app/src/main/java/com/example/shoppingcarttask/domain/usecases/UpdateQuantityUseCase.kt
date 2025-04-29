package com.example.shoppingcarttask.domain.usecases

import com.example.shoppingcarttask.domain.models.Cart
import javax.inject.Inject

class UpdateQuantityUseCase @Inject constructor() {
    operator fun invoke(cart: List<Cart>, updatedItem: Cart): List<Cart> {
        return cart.map {
            if (it.id == updatedItem.id) it.copy(quantity = updatedItem.quantity) else it
        }
    }
}