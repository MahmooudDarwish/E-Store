package com.example.e_store.features.orders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.e_store.utils.shared_components.ProductPrice
import com.example.e_store.utils.shared_methods.changeDateFormat
import com.example.e_store.utils.shared_models.Order

@Composable
fun OrderSummary(order: Order, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = changeDateFormat(order.created_at.toString()),
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProductPrice(price = order.current_total_price.toString(), isHorizontalItem = true, isOrder = true )

            Text(
                text = "· ${order.line_items?.size} items",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                )
            )
        }
    }
}
