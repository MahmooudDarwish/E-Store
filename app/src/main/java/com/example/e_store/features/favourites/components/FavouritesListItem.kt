package com.example.e_store.features.favourites.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_store.R
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.ConfirmNegativeActionDialog
import com.example.e_store.utils.shared_components.ProductPrice
import com.example.e_store.utils.shared_components.ProductTitle
import com.example.e_store.utils.shared_methods.convertCurrency
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.ProductDetails

@Composable
fun FavouritesListItem(
    lineItem: LineItem,
    onDeleteClick: () -> Unit,
    navController: NavHostController,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val currentRoute = navController.currentBackStackEntry?.destination?.route.toString()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            lineItem.product_id?.let { productId ->
                ProductDetails.id = productId
                ProductDetails.isNavigationFromFavourites = true

                when {
                    currentRoute.contains(Screen.Home.route) -> {
                        Log.d("FavouritesListItem", "FavouritesListItem1: $currentRoute")
                        navController.navigate(Screen.ProductInfoFromHome.route)
                    }

                    currentRoute.contains(Screen.Categories.route) -> {
                        Log.d("FavouritesListItem", "FavouritesListItem2: $currentRoute")
                        navController.navigate(Screen.ProductInfoFromCategories.route)
                    }

                    currentRoute.contains(Screen.Profile.route) -> {
                        Log.d("FavouritesListItem", "FavouritesListItem3: $currentRoute")
                        navController.navigate(Screen.ProductInfoFromProfile.route)
                    }

                    else -> {
                        Log.d("FavouritesListItem", "FavouritesListItem4: $currentRoute")
                        navController.navigate(Screen.ProductInfoFromHome.route)
                    }
                }

            }
        }
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = lineItem.properties[2].value
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = lineItem.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))



            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
            ) {
                ProductTitle(title = lineItem.title)

                Spacer(modifier = Modifier.height(4.dp))
                ProductPrice(price =lineItem.price , isHorizontalItem = true)
                //convertCurrency(lineItem.price.toDouble())
            }

            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = CircleShape
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Favourite",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }

    ConfirmNegativeActionDialog(
        showDialog = showDeleteDialog,
        title = stringResource(R.string.remove_from_favourites),
        message = stringResource(R.string.are_you_sure_you_want_to_remove_this_item_from_your_favourites),
        onConfirm = {
            onDeleteClick()
        },
        onDismiss = {
            showDeleteDialog = false
        }
    )

}
