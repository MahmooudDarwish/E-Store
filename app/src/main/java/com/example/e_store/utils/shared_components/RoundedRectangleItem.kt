package com.example.e_store.utils.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.e_store.utils.shared_models.Product

@Composable
fun RoundedRectangleItem(
    product: Product,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image
        AsyncImage(
            model = product.image.src,
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth(),
        )

        Gap(height = 8)

        ProductTitle(title = product.title, modifier = Modifier.padding(horizontal = 5.dp))

        ProductPrice(price = product.variants[0].price, isHorizontalItem = false)
    }

}