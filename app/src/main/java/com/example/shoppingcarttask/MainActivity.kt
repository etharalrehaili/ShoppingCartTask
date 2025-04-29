package com.example.shoppingcarttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shoppingcarttask.presentation.ui.CartScreen
import com.example.shoppingcarttask.ui.theme.ShoppingCartTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingCartTaskTheme {
                CartScreen()
            }
        }
    }
}
