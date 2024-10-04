package com.example.e_store.features.brand_products.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product

@Composable
fun BrandProductsStateHandler(
    brandProductsUiState: DataState<List<Product>>,
    navController: NavHostController,
    filterProducts: (List<Product>) -> List<Product>
) {
    when (brandProductsUiState) {
        DataState.Loading -> {
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val brandProducts = brandProductsUiState.data
            ProductsGrid(
                navController = navController,
                products = filterProducts(brandProducts)
            )
        }

        is DataState.Error -> {
            val errorMsg = brandProductsUiState.message
            val context = LocalContext.current
            LaunchedEffect(errorMsg) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
