package com.example.shoppingcarttask

import com.example.shoppingcarttask.data.repository.CartRepository
import com.example.shoppingcarttask.domain.models.Cart
import com.example.shoppingcarttask.domain.usecases.GetCartItemsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCartItemsUseCaseTest {

    private val repository: CartRepository = mockk()
    private val getCartItemsUseCase = GetCartItemsUseCase(repository)

    @Test
    fun `get cart items returns the correct items from the repository`() = runBlocking {
        val mockCart = listOf(
            Cart(1, "T-Shirt", 79.99, 2),
            Cart(2, "Pants", 99.99, 1)
        )

        // Mock the repository to return the predefined list of items
        coEvery { repository.getCart() } returns mockCart

        // Fetch the cart items using the use case
        val result = getCartItemsUseCase()

        // Assert that the result contains the same items as the mock
        assertEquals(mockCart, result)
    }
}
