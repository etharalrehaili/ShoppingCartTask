package com.example.shoppingcarttask.data.repository

import com.example.shoppingcarttask.data.datasource.CartDataSource
import com.example.shoppingcarttask.domain.models.CartItem
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val localDataSource: CartDataSource
) : CartRepository {

    override suspend fun getCartItems(): List<CartItem> = localDataSource.getItems()

    override suspend fun addItem(item: CartItem) = localDataSource.insertItem(item)

    override suspend fun removeItem(itemId: String) = localDataSource.deleteItem(itemId)
}