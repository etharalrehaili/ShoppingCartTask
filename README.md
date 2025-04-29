# Shopping Cart App
A simple shopping cart app using Kotlin, Jetpack Compose, and MVI architecture with Hilt for dependency injection.

## Project Overview
This app demonstrates the use of Model-View-Intent (MVI) architecture to manage UI state in a scalable and predictable way. It features a cart system where users can load cart items, add new items, and potentially remove or update them.

## MVI Structure
Model (Domain Layer): Represents the business logic and data models.
View (UI Layer): Composables that render the UI based on the state.
Intent (Presentation Layer): User or system events (add an item, load cart).
State: Represents the current UI state (loading, success, error).
ViewModel: Receives intents, processes logic, and updates state accordingly.

## Key Components
### Intent
Represents user actions or events.

```
sealed class CartIntent {
    object LoadCart : CartIntent()
    data class AddItem(val item: Cart) : CartIntent()
    data class RemoveItem(val item: Cart) : CartIntent()
    data class UpdateQuantity(val item: Cart) : CartIntent()
}
```

### State
Defines the possible UI states.

```
data class CartState(
    val loading: Boolean = false,
    val cart: List<Cart> = emptyList(),
    val error: String? = null,
)
```

### ViewModel
Acts as a bridge

```
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
```

### View (UI)
have the user interface

```
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

```


## Screenshots

![image](https://github.com/user-attachments/assets/c08d3258-6fa8-4176-b9f7-7bd629edc8ca)

