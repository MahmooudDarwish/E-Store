package com.example.e_store.features.shopping_cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.e_store.R
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_components.ProductPrice
import com.example.e_store.utils.shared_components.ProductTitle
import com.example.e_store.utils.shared_models.LineItem

@Composable
fun ProductInfo(
    item: LineItem,
    viewModel: ShoppingCartViewModel,
    onItemDeleted: (LineItem) -> Unit, modifier: Modifier,
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        ProductTitle(title = item.title)
        ProductPrice(
            price =  (item.price.toDouble() * item.quantity).toString(),
            isHorizontalItem = true
        )


        Gap(height = 8)

        QuantityControl(
            quantity = item.quantity,
            onIncrease = {
                item.product_id?.let {
                    viewModel.increaseProductQuantity(it, item.variant_id.toLong(), context)
                }
            },
            onDecrease = {
                item.product_id?.let {
                    viewModel.decreaseProductQuantity(it, item.variant_id.toLong())
                }
                if (item.quantity == 0) onItemDeleted(item)
            }
        )
    }
}


