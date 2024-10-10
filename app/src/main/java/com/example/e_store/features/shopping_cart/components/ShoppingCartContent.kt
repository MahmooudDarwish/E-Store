package com.example.e_store.features.shopping_cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.utils.shared_components.EShopButton
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_methods.convertCurrency
import com.example.e_store.utils.shared_models.LineItem

@Composable
fun ShoppingCartContent(
    items: List<LineItem>,
    viewModel: ShoppingCartViewModel,
    onCheckout: () -> Unit
) {
    var showAllItems by remember { mutableStateOf(false) }

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
            val itemsToShow = if (showAllItems) items else items.take(2)
            items(itemsToShow.size) { currentItem ->
                val item = itemsToShow[currentItem]
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

        if (items.size > 2) {
            Text(
                text = if (showAllItems) "See Less" else "See More",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        showAllItems = !showAllItems
                    }
                    .padding(vertical = 8.dp)
            )
        }

        Text(
            text = "Total price: ${convertCurrency(viewModel.getTotalPrice())}",
            )



        Gap(height = 8)

        EShopButton(text = "CHECKOUT", onClick = onCheckout)
    }
}