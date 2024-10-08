package com.example.e_store.features.shopping_cart.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.e_store.R
import com.example.e_store.utils.shared_components.LottieWithText

@Composable
fun EmptyCartView() {
    LottieWithText(
        displayText = stringResource(id = R.string.no_products_shopping_message),
        lottieRawRes = R.raw.no_data_found
    )
}
