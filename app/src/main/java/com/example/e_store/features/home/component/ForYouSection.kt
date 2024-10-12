package com.example.e_store.features.home.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.dp
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_models.Product
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.RoundedRectangleItem
import com.example.e_store.utils.shared_methods.initializeProductDetails
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_view_model.FavouriteControllerViewModel
import kotlinx.coroutines.delay

@Composable
fun ForUSection(
    navController: NavHostController, products: List<Product>, viewModel: FavouriteControllerViewModel
) {
    var isVisible by remember { mutableStateOf(false) }
    var draftOrderItems by remember { mutableStateOf<List<DraftOrderDetails>>(emptyList()) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true

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
                .fillMaxWidth()
                .heightIn(max = 500.dp).padding(horizontal = 20.dp),
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
                            navController.navigate(NavigationKeys.PRODUCT_INFO_HOME_ROUTE)
                        }, // Pass draft orders here
                        navController = navController,
                        viewModel =  viewModel,
                    )
                }
            }
        }
    }
}





