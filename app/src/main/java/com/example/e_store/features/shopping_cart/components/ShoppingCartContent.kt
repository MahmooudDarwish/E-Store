package com.example.e_store.features.shopping_cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.utils.shared_components.EShopButton
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.LineItem

@Composable
fun ShoppingCartContent(
    items: List<LineItem>,
    viewModel: ShoppingCartViewModel,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.size) { currentItem ->
                val item = items[currentItem]
                ShoppingCartItem(
                    item = item,
                    viewModel = viewModel,
                    onItemDeleted = {
                        viewModel.removeShoppingCartDraftOrder(
                            productId = item.product_id!!,
                            variantId = item.variant_id.toLong()
                        )
                    }
                )
            }
        }

        Text(
            text = "Total price: ${viewModel.getTotalPrice()} EGP",
        )

        Gap(height = 8)
        EShopButton(text = "CHECKOUT", onClick = onCheckout)
    }
}
