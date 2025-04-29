package com.example.shoppingcarttask

import com.example.shoppingcarttask.domain.models.CartItem

sealed class CartIntent {
    data class AddItem(val item: CartItem) : CartIntent()
    data class RemoveItem(val itemId: String) : CartIntent()
    data class UpdateQuantity(val itemId: String, val newQuantity: Int) : CartIntent()
    object LoadCart : CartIntent()
}
