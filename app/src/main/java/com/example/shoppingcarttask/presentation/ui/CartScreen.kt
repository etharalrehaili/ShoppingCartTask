package com.example.shoppingcarttask.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import com.example.shoppingcarttask.CartIntent
import com.example.shoppingcarttask.domain.models.Cart
import com.example.shoppingcarttask.presentation.viewmodel.MainViewModel

@Composable
fun CartScreen(mainViewModel: MainViewModel) {
    val state by mainViewModel.state.collectAsState()

    // State variables for the input fields
    var itemName by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }
    var quantityInput by remember { mutableStateOf("") }

    LaunchedEffect(mainViewModel) {
        mainViewModel.handleIntent(CartIntent.LoadCart)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.loading -> {
                // Display a loading indicator
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            state.error != null -> {
                // Display an error message
                Text(text = "Error: ${state.error}", color = Color.Red)
            }

            else -> {
                // Display the list of cart items
                CartList(
                    cart = state.cart,
                    onRemove = { item -> mainViewModel.handleIntent(CartIntent.RemoveItem(item)) },
                    onUpdate = { item -> mainViewModel.handleIntent(CartIntent.UpdateQuantity(item)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields
        TextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = priceInput,
            onValueChange = { priceInput = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = quantityInput,
            onValueChange = { quantityInput = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Add Item button
        Button(onClick = {
            val price = priceInput.toDoubleOrNull()
            val quantity = quantityInput.toIntOrNull()

            // Ensure all fields are filled correctly
            if (itemName.isNotBlank() && price != null && quantity != null) {
                val newItem = Cart(
                    id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(), // Generate a simple ID
                    itemName = itemName,
                    price = price,
                    quantity = quantity
                )
                mainViewModel.handleIntent(CartIntent.AddItem(newItem))

                // Clear inputs
                itemName = ""
                priceInput = ""
                quantityInput = ""
            }
        }) {
            Text("Add Item")
        }
    }
}

@Composable
fun CartList(
    cart: List<Cart>,
    onRemove: (Cart) -> Unit,
    onUpdate: (Cart) -> Unit
) {
    LazyColumn {
        items(cart) { item ->
            var newQuantity by remember { mutableStateOf(item.quantity.toString()) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Id: ${item.id}")
                    Text(text = "Name: ${item.itemName}")
                    Text(text = "Price: ${item.price}")

                    TextField(
                        value = newQuantity,
                        onValueChange = { newQuantity = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(onClick = {
                        val quantityInt = newQuantity.toIntOrNull()
                        if (quantityInt != null) {
                            onUpdate(item.copy(quantity = quantityInt))
                        }
                    }) {
                        Text("Update Quantity")
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(onClick = { onRemove(item) }) {
                        Text("Remove Item")
                    }
                }
            }
        }
    }
}
