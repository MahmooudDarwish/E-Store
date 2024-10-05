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
import androidx.navigation.NavHostController
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.RoundedRectangleItem
import com.example.e_store.utils.shared_models.ProductDetails

@Composable
fun ForUSection(
    navController: NavHostController, products: List<Product>
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
                RoundedRectangleItem(
                    product = product,
                    onClick = {
                        ///TODO: Navigation to product details screen  @MahmoudDarwish @kk98989898})
                        initializeProductDetails(product)
                        navController.navigate(NavigationKeys.PRODUCT_INFO_ROUTE)
                    },
                )
            }
        }
    }
}


fun initializeProductDetails(product: Product) {
    ProductDetails.title = product.title
    ProductDetails.vendor = product.vendor
    ProductDetails.price = product.variants[0].price
    //ProductDetails.description = product.bodyHtml
    ProductDetails.stock = product.variants[0].inventoryQuantity
    ProductDetails.images = product.images.map { it.src }
    ProductDetails.colors = product.options[1].values
    ProductDetails.sizes = product.options[0].values
    ProductDetails.variants = product.variants
}


