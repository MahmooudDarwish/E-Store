package com.example.e_store.features.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.dp
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_models.Product
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.e_store.utils.shared_components.RoundedRectangleItem

@Composable
fun ForUSection(
    onProductClick: (Product) -> Unit, products: List<Product>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ElevationCard(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                RoundedRectangleItem(product = product, onClick = { onProductClick(product) })
            }
        }
    }
}


