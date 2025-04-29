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
    data class AddItem(val item: CartItem) : CartIntent()
    data class RemoveItem(val itemId: String) : CartIntent()
    data class UpdateQuantity(val itemId: String, val newQuantity: Int) : CartIntent()
    object LoadCart : CartIntent()
}
```

### State
Defines the possible UI states.

```
sealed class CartState {
    object Loading : CartState()
    data class Success(val items: List<CartItem>) : CartState()
    data class Error(val message: String) : CartState()
}
```

### ViewModel
Acts as a bridge

```
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

                else -> {} // Other intents can be handled similarly
            }
        }
    }
}
```

### View (UI)
have the user interface

```
@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState()

    when (val s = state.value) {
        is CartState.Loading -> CircularProgressIndicator()
        is CartState.Success -> CartList(items = s.items)
        is CartState.Error -> Text("Error: ${s.message}")
    }
}

@Composable
fun CartList(items: List<CartItem>) {
    LazyColumn {
        items(items) { item ->
            Text("${item.name} - Qty: ${item.quantity} - $${item.price}")
        }
    }
}

```

