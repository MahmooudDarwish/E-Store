package com.example.e_store.utils.shared_components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.e_store.ui.theme.PrimaryColor

@Composable
fun ProductPrice(price: String, isHorizontalItem: Boolean, isOrder: Boolean = false) {
    if (isHorizontalItem) {
        Text(
            text = "$price USD",
            style = if (isOrder) {
                MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            } else {
                MaterialTheme.typography.bodyMedium
            },
            color = MaterialTheme.colorScheme.secondary
        )
    } else {
        Text(
            text = "$price USD",
            color = PrimaryColor
        )
    }
}
