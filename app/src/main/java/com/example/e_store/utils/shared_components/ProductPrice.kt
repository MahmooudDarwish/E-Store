package com.example.e_store.utils.shared_components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_methods.convertCurrency

@Composable
fun ProductPrice(price: String, isHorizontalItem: Boolean, isOrder: Boolean = false) {
    if (isHorizontalItem) {
        Text(
            text = convertCurrency(price.toDouble())  ,
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
            text = convertCurrency(price.toDouble()),
            color = PrimaryColor
        )
    }
}
