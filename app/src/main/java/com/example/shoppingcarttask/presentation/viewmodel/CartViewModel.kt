package com.example.shoppingcarttask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcarttask.CartIntent
import com.example.shoppingcarttask.domain.usecases.AddItemToCartUseCase
import com.example.shoppingcarttask.domain.usecases.GetCartItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CartState>(CartState.Loading)
    val state: StateFlow<CartState> get() = _state

    fun processIntent(intent: CartIntent) {
        viewModelScope.launch {
            when (intent) {
                is CartIntent.LoadCart -> {
                    _state.value = CartState.Loading
                    try {
                        val items = getCartItemsUseCase()
                        _state.value = CartState.Success(items)
                    } catch (e: Exception) {
                        _state.value = CartState.Error("Failed to load cart")
                    }
                }
                is CartIntent.AddItem -> {
                    addItemToCartUseCase(intent.item)
                    processIntent(CartIntent.LoadCart)
                }
                // Handle other intents similarly
                else -> {}
            }
        }
    }
}