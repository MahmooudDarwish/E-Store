package com.example.e_store.utils.shared_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_components.RoundedRectangleItem
import com.example.e_store.utils.shared_methods.initializeProductDetails
import com.example.e_store.utils.shared_models.Product

@Composable
fun ProductsGrid(
    navController: NavHostController, route: String, products: List<Product>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(700.dp)

            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ElevationCard(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                RoundedRectangleItem(product = product, onClick = {
                    initializeProductDetails(product)
                    navController.navigate(route)

                })
            }
        }
    }
}


