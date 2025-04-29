package com.example.shoppingcarttask.domain.usecases

import com.example.shoppingcarttask.data.repository.CartRepository
import com.example.shoppingcarttask.domain.models.CartItem
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(): List<CartItem> = repository.getCartItems()
}

class AddItemToCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) = repository.addItem(item)
}
