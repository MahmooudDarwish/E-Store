package com.example.e_store.utils.shared_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.utils.shared_methods.initializeProductDetails
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_view_model.FavouriteControllerViewModel
import kotlinx.coroutines.delay

@Composable
fun ProductsGrid(
    navController: NavHostController,
    route: String,
    products: List<Product>,
    viewModel: FavouriteControllerViewModel,
) {
    var isVisible by remember { mutableStateOf(false) }
    var draftOrderItems by remember { mutableStateOf<List<DraftOrderDetails>>(emptyList()) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true

        // Fetch draft orders only once
        viewModel.fetchFavouritesFromDraftOrder()
        viewModel.draftOrderItems.collect { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    draftOrderItems = dataState.data.draft_orders
                }

                else -> {
                }
            }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .height(700.dp)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ElevationCard(
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    RoundedRectangleItem(
                        product = product,
                        viewModel = viewModel,
                        navController = navController,
                        onClick = {
                            initializeProductDetails(product)
                            navController.navigate(route)
                        }
                    )
                }
            }
        }
    }
}