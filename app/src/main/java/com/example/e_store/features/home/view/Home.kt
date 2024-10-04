package com.example.e_store.features.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Welcome to Home Screen",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )


    }
}
