package com.example.shoppingcarttask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcarttask.CartIntent
import com.example.shoppingcarttask.domain.models.Cart
import com.example.shoppingcarttask.domain.usecases.CartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: CartUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    fun handleIntent(intent: CartIntent) {
        viewModelScope.launch {
            when (intent) {
                is CartIntent.LoadCart -> fetchCart()
                is CartIntent.AddItem -> addToCart(intent.item)
                is CartIntent.RemoveItem -> removeFromCart(intent.item)
                is CartIntent.UpdateQuantity -> updateQuantity(intent.item)
            }
        }
    }

    private suspend fun fetchCart() {
        _state.value = _state.value.copy(loading = true, error = null)
        try {
            val cart = useCases.getCartItems()
            _state.value = CartState(loading = false, cart = cart)
        } catch (e: Exception) {
            _state.value = CartState(loading = false, error = e.message)
        }
    }

    private suspend fun addToCart(item: Cart) {
        val updatedCart = useCases.addItemToCart(_state.value.cart, item)
        _state.value = _state.value.copy(cart = updatedCart)
    }

    private suspend fun removeFromCart(item: Cart) {
        val updatedCart = useCases.removeItemFromCart(_state.value.cart, item)
        _state.value = _state.value.copy(cart = updatedCart)
    }

    private suspend fun updateQuantity(item: Cart) {
        val updatedCart = useCases.updateQuantity(_state.value.cart, item)
        _state.value = _state.value.copy(cart = updatedCart)
    }

}