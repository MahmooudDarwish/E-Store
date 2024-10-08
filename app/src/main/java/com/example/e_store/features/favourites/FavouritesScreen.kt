package com.example.e_store.features.favourites

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.ProductDetails
import androidx.compose.material3.*
import androidx.compose.runtime.*


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
    ) {
        FavouritesHeader(navController)


        when {
            allDraftItems is DataState.Loading -> {
                EShopLoadingIndicator()
            }

            allDraftItems is DataState.Success -> {
                if (favourites.isEmpty()) {
                    EmptyFavouritesIndicator()
                } else {
                    FavouritesList(
                        navController = navController,
                        favourites = favourites,
                        onDeleteClick = { productId, variantId ->
                            viewModel.removeFavoriteDraftOrderLineItem(productId, variantId)
                            Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }
            }

            allDraftItems is DataState.Error -> {
                ErrorView(message = (allDraftItems as DataState.Error).message.toString())
            }
        }
    }

}

@Composable
fun FavouritesHeader(navController: NavHostController) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBackClick = { navController.popBackStack() })

        Text(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            text = "Favourites",
            style = MaterialTheme.typography.headlineLarge,
            color = PrimaryColor,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun EmptyFavouritesIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Empty Favourites",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp),
            tint = PrimaryColor
        )
        Text(
            text = "Your favourites list is empty",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = PrimaryColor
        )
        Text(
            text = "Add items to your favourites to see them here",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
            color = PrimaryColor
        )
    }
}

@Composable
fun FavouritesList(
    navController: NavHostController,
    favourites: List<DraftOrderDetails>,
    onDeleteClick: (Long, Long) -> Unit
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

@Composable
fun FavouritesListItem(
    lineItem: LineItem,
    onDeleteClick: () -> Unit,
    navController: NavHostController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            lineItem.product_id?.let {
                ProductDetails.id = it
                ProductDetails.isNavigationFromFavourites = true
                navController.navigate(Screen.ProductInfoFromHome.route)
            }
        }
    ) {
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
                Text(
                    text = lineItem.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${lineItem.price} USD",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Remove from Favourites",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove this item from your favourites?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Remove", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

}

@Composable
fun ErrorView(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Error",
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.error
        )
    }
}

