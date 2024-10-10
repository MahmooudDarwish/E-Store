package com.example.e_store.features.favourites.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.favourites.components.EmptyFavouritesIndicator
import com.example.e_store.features.favourites.components.ErrorView
import com.example.e_store.features.favourites.components.FavouritesHeader
import com.example.e_store.features.favourites.components.FavouritesList
import com.example.e_store.features.favourites.view_model.FavouritesViewModel
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_models.DataState


@Composable
fun FavouritesScreen(viewModel: FavouritesViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val allDraftItems by viewModel.draftOrderItems.collectAsState()
    val favourites by viewModel.favourites.collectAsState()

    // Fetch the favourites when the screen is first loaded
    LaunchedEffect(Unit) {
        viewModel.fetchFavouritesFromDraftOrder()
    }


    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 24.sp,
            color = PrimaryColor,
            fontStyle = FontStyle.Italic
        )

        FavouritesHeader(navController)


        when {
            allDraftItems is DataState.Loading -> {
                EShopLoadingIndicator()
            }

            allDraftItems is DataState.Success -> {
                if (favourites.isEmpty()) {
                    EmptyFavouritesIndicator()
                } else {
                    FavouritesList(navController = navController,
                        favourites = favourites,
                        onDeleteClick = { productId, variantId ->
                            viewModel.removeFavoriteDraftOrderLineItem(productId, variantId)
                            Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT)
                                .show()
                        })
                }
            }

            allDraftItems is DataState.Error -> {
                ErrorView(message = (allDraftItems as DataState.Error).message.toString())
            }
        }
    }

}










