package com.example.shoppingcarttask.domain.usecases

import javax.inject.Inject

data class CartUseCases @Inject constructor(
    val getCartItems: GetCartItemsUseCase,
    val addItemToCart: AddItemToCartUseCase,
    val removeItemFromCart: RemoveItemFromCartUseCase,
    val updateQuantity: UpdateQuantityUseCase
)