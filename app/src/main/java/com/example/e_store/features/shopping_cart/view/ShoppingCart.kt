package com.example.e_store.features.shopping_cart.view

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.e_store.features.shopping_cart.components.EmptyCartView
import com.example.e_store.features.shopping_cart.components.LoadingDialog
import com.example.e_store.features.shopping_cart.components.ShoppingCartContent
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails


@Composable
fun ShoppingCartScreen(viewModel: ShoppingCartViewModel,navController: NavHostController) {
    val shoppingCartItemsUiState by viewModel.shoppingCartItems.collectAsStateWithLifecycle()
    val loadingProcessUiState by viewModel.applyChangesLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingCartItems(isLoading = true)
    }

    when (shoppingCartItemsUiState) {
        is DataState.Loading -> EShopLoadingIndicator()

        is DataState.Success -> {
            val items = (shoppingCartItemsUiState as DataState.Success<DraftOrderDetails?>)
                .data?.line_items
            if (items.isNullOrEmpty()) {
                EmptyCartView()
            } else {
                ShoppingCartContent(
                    items = items,
                    viewModel = viewModel,
                    onCheckout = { navController.navigate(NavigationKeys.CHECKOUT_ROUTE) }
                )
            }
        }

        is DataState.Error -> {
            val error = (shoppingCartItemsUiState as DataState.Error).message
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    if (loadingProcessUiState) {
        LoadingDialog()
    }
}
