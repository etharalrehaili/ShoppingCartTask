package com.example.shoppingcarttask

import com.example.shoppingcarttask.domain.models.CartItem

sealed class CartIntent {
    object LoadCart : CartIntent()
    data class AddItem(val item: Cart) : CartIntent()
    data class RemoveItem(val item: Cart) : CartIntent()
    data class UpdateQuantity(val item: Cart) : CartIntent()
}
