package com.example.shoppingcarttask.domain.usecases

import com.example.shoppingcarttask.domain.models.Cart
import javax.inject.Inject

class RemoveItemFromCartUseCase @Inject constructor() {
    operator fun invoke(cart: List<Cart>, itemToRemove: Cart): List<Cart> {
        return cart.filterNot { it.id == itemToRemove.id }
    }
}