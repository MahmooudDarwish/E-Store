package com.example.e_store.utils.shared_components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product

@Composable
fun ProductsStateHandler(
    productsUiState: DataState<List<Product>>,
    navController: NavHostController,
    route: String,
    filterProducts: (List<Product>) -> List<Product>
) {
    when (productsUiState) {
        DataState.Loading -> {
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val brandProducts = productsUiState.data
            ProductsGrid(
                navController = navController,
                route = route,
                products = filterProducts(brandProducts)
            )
        }

        is DataState.Error -> {
            val errorMsg = productsUiState.message
            val context = LocalContext.current
            LaunchedEffect(errorMsg) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}