package com.example.e_store.utils.shared_components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.e_store.R
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.ProductDetails
import com.example.e_store.utils.shared_models.Property
import com.example.e_store.utils.shared_models.UserSession
import com.example.e_store.utils.shared_view_model.FavouriteControllerViewModel

@Composable
fun RoundedRectangleItem(
    product: Product,
    viewModel: FavouriteControllerViewModel,
    onClick: () -> Unit,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val draftOrderItems by viewModel.favourites.collectAsState()
    val isLoadingToggle by viewModel.isLoadingToggle.collectAsState()
    var showLoginDialog by remember { mutableStateOf(false) }

    val isFavorite by remember(draftOrderItems, product) {
        derivedStateOf {
            viewModel.checkIfItemIsInFavouriteDraftOrder(
                productId = product.id,
                variantId = product.variants.first().id
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                model = product.image.src,
                contentDescription = product.title,
                modifier = Modifier.fillMaxWidth()
            )

            when (isLoadingToggle) {
                true -> {
                    if (UserSession.isGuest) {
                        showLoginDialog = true
                    } else {
                        IconButton(onClick = {
                            Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                                contentDescription = null,
                                tint = if (isFavorite) PrimaryColor else Color.Gray
                            )
                        }
                    }
                }

                false -> {
                    IconButton(onClick = {
                        if (UserSession.isGuest) {
                            showLoginDialog = true
                        } else

                            if (isFavorite) {
                                viewModel.removeFavoriteDraftOrderLineItem(
                                    product.id,
                                    product.variants.first().id
                                )
                            } else {
                                viewModel.createDraftOrder(
                                    lineItemList = createDraftOrderItems(
                                        quantity = 1,
                                        price = product.variants.first().price,
                                        variantId = product.variants.first().id.toString(),
                                        selectedSize = product.variants.first().option1.toString(),
                                        selectedColor = product.variants.first().option2.toString(),
                                        image = product.image.src,
                                    ),
                                    isFavorite = true,
                                    productID = product.id
                                )
                            }

                    }) {

                        Log.d("DEBUGGG ", "isFavorite: $isFavorite")
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = if (isFavorite) PrimaryColor else Color.Gray
                        )
                    }
                }
            }
        }

        // Product details
        ProductTitle(title = product.title, modifier = Modifier.padding(horizontal = 5.dp))
        ProductPrice(price = product.variants.first().price, isHorizontalItem = false)

        // Alert Popup for login

        Popup(
            showDialog = showLoginDialog,
            onDismiss = { showLoginDialog = false },
            title = stringResource(R.string.login_required),
            body = stringResource(R.string.you_need_to_login_to_use_this_feature),
            confirmButtonText = stringResource(R.string.sign_in),
            onConfirm = {
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            },
            dismissButtonText = stringResource(R.string.cancel)
        )

    }
}


fun createDraftOrderItems(
    selectedSize: String,
    selectedColor: String,
    price: String,
    variantId: String,
    image: String,
    quantity: Int,
): MutableList<LineItem> {
    val lineItemsList = mutableListOf<LineItem>()

    // Find the matching variant based on selected size and color
    val variant =
        ProductDetails.variants.find { it.option1 == selectedSize && it.option2 == selectedColor }
    Log.d("ProductInfoScreen", "variantId: $variantId")
    Log.d("ProductInfoScreen", "price: ${price}")
    Log.d("ProductInfoScreen", "variantId: $selectedSize")
    Log.d("ProductInfoScreen", "variantId: $selectedColor")


    val properties: List<Property> = listOf(
        Property("Size", selectedSize),    // Capitalized "Size"
        Property("Color", selectedColor),  // Capitalized "Color"
        Property("imageUrl", image)
    )

    // Create a line item and add it to the list
    val lineItem = LineItem(
        title = ProductDetails.title,
        price = price,
        quantity = quantity,
        variant_id = variantId,
        properties = properties
    )

    lineItemsList.add(lineItem)

    return lineItemsList
}

