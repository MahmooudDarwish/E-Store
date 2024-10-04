package com.example.e_store.utils.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_models.Product

@Composable
fun RoundedRectangleItem(
    product: Product,
    onClick: () -> Unit
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

        // Product name
        Text(
            text = product.title,
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )

        // Product price
        Text(
            ///TODO: Change the currency using the same method @mohamed-abdelrehim142000
            text = "${product.variants[0].price} USD",
            color = PrimaryColor
        )
    }

}