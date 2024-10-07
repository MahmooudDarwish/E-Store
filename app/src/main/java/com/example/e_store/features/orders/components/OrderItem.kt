package com.example.e_store.features.orders.components

import Order
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.e_store.utils.shared_components.ElevationCard

@Composable
fun OrderItem(order: Order) {
    var isExpanded by remember { mutableStateOf(false) }

    ElevationCard(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            OrderHeader(
                order = order,
                isExpanded = isExpanded,
                onClickExpand = { isExpanded = !isExpanded }
            )

            if (isExpanded) {
                OrderDetails(order)
            }
        }
    }
}


