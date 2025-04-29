package com.example.shoppingcarttask

import com.example.shoppingcarttask.domain.models.Cart
import com.example.shoppingcarttask.domain.usecases.AddItemToCartUseCase
import org.junit.Assert.*
import org.junit.Test

class AddItemToCartUseCaseTest {

    private val useCase = AddItemToCartUseCase()

    @Test
    fun `add item appends it to the cart`() {
        val cart = listOf(Cart(1, "T-Shirt", 79.99, 2))
        val newItem = Cart(2, "Pants", 99.99, 1)

        val result = useCase(cart, newItem)

        assertEquals(2, result.size)
        assertTrue(result.contains(newItem))
    }
}
