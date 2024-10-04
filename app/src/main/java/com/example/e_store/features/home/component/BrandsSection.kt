package com.example.e_store.features.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.e_store.utils.shared_components.CircularItem
import com.example.e_store.utils.shared_models.Brand

@Composable
fun BrandsSection(onBrandClick: (Brand) -> Unit, brands: List<Brand>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        itemsIndexed(brands) { _, brand ->
            CircularItem(
                title = brand.title,
                imageUrl = brand.image!!.src,
                onClick = { onBrandClick(brand) }
            )
        }
    }
}

