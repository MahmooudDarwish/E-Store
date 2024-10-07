package com.example.e_store.features.orders.components

import Order
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrderHeader(order: Order, isExpanded: Boolean, onClickExpand: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrderSummary(order, modifier = Modifier.weight(1f))

        OrderID(name = order.name!!)

        ExpandCollapseIcon(isExpanded, onClickExpand)
    }
}