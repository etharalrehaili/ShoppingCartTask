package com.example.shoppingcarttask

import com.example.shoppingcarttask.domain.models.Cart
import com.example.shoppingcarttask.domain.usecases.RemoveItemFromCartUseCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlin.test.Test

class RemoveItemFromCartUseCaseTest {

    private val removeItemFromCartUseCase = RemoveItemFromCartUseCase()

    @Test
    fun `remove item removes the item from the cart`() {
        val initialCart = listOf(
            Cart(1, "T-Shirt", 79.99, 2),
            Cart(2, "Pants", 99.99, 1)
        )
        val itemToRemove = Cart(1, "T-Shirt", 79.99, 2)

        val result = removeItemFromCartUseCase(initialCart, itemToRemove)

        // Assert that the result does not contain the item to remove
        assertFalse(result.contains(itemToRemove))

        // Assert that the result size is now 1
        assertEquals(1, result.size)
    }
}
