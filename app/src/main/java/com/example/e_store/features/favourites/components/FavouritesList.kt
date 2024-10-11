package com.example.e_store.features.favourites.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.utils.shared_models.DraftOrderDetails

@Composable
fun FavouritesList(
    navController: NavHostController,
    favourites: List<DraftOrderDetails>,
    onDeleteClick: (Long, Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(favourites) { draftOrder ->
            draftOrder.line_items.forEach { lineItem ->
                FavouritesListItem(
                    navController = navController,
                    lineItem = lineItem,
                    onDeleteClick = {
                        lineItem.product_id?.let { onDeleteClick(it, lineItem.variant_id.toLong()) }
                    }
                )
            }
        }
    }
}