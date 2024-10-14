package com.example.e_store.utils.shared_components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_view_model.FavouriteControllerViewModel

@Composable
fun ProductsStateHandler(
    productsUiState: DataState<List<Product>>,
    navController: NavHostController,
    route: String,
    filterProducts: (List<Product>) -> List<Product>,
    viewModel: FavouriteControllerViewModel
) {



    when (productsUiState) {
        DataState.Loading -> {
            EShopLoadingIndicator()
        }


        is DataState.Success -> {
            val brandProducts = productsUiState.data
            val filteredProducts = filterProducts(brandProducts)

            if (filteredProducts.isEmpty()) {
                LottieWithText(
                    displayText = stringResource(id = R.string.no_products_message),
                    lottieRawRes = R.raw.no_data_found
                )
            } else {
                ProductsGrid(
                    navController = navController,
                    route = route,
                    products = filteredProducts,
                    viewModel = viewModel
                )
            }

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