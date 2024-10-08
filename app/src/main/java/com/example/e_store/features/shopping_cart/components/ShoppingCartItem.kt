package com.example.e_store.features.shopping_cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.e_store.utils.shared_models.LineItem
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.utils.shared_components.ElevationCard
import kotlinx.coroutines.launch

@Composable
fun ShoppingCartItem(
    item: LineItem,
    viewModel: ShoppingCartViewModel,
    onItemDeleted: (LineItem) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        ConfirmDeleteDialog(
            item = item,
            onConfirm = {
                coroutineScope.launch { onItemDeleted(item) }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    ElevationCard() {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(imageUrl = item.properties.getOrNull(2)?.value ?: "")

            Spacer(modifier = Modifier.width(8.dp))

            ProductInfo(
                item = item,
                viewModel = viewModel,
                onItemDeleted = onItemDeleted,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}
