package com.example.shoppingcarttask.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.items
import com.example.shoppingcarttask.domain.models.CartItem
import com.example.shoppingcarttask.presentation.viewmodel.CartState
import com.example.shoppingcarttask.presentation.viewmodel.CartViewModel

@Composable
fun CartList(items: List<CartItem>) {
    LazyColumn {
        items(items) { item ->
            Text("${item.name} - Qty: ${item.quantity} - $${item.price}")
        }
    }
}

@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState()

    when (val s = state.value) {
        is CartState.Loading -> CircularProgressIndicator()
        is CartState.Success -> CartList(items = s.items)
        is CartState.Error -> Text("Error: ${s.message}")
    }
}

