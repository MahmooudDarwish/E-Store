package com.example.e_store.features.orders.components

import Order
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.e_store.utils.constants.Keys
import com.example.e_store.utils.shared_models.UserSession

@Composable
fun OrderDetails(order: Order) {
    Column(modifier = Modifier.fillMaxWidth()) {

        HorizontalDivider()
        Text(
            text = UserSession.name!!,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            order.line_items?.forEach { lineItem ->
                lineItem.properties?.forEach { property ->
                    if (property.name == Keys.IMAGE_URL_KEY) {
                        item {
                            AsyncImage(
                                model = property.value,
                                contentDescription = "Item Image",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}