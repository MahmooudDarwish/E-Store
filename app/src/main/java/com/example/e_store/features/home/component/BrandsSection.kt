package com.example.e_store.features.home.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.CircularItem
import com.example.e_store.utils.shared_models.Brand

@Composable
fun BrandsSection(navController: NavHostController, brands: List<Brand>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
            itemsIndexed(brands) { _, brand ->
                    CircularItem(
                        title = brand.title,
                        imageUrl = brand.image!!.src,
                        onClick = {
                            Log.d("BRAND_ID", brand.id.toString())
                            navController.navigate(
                                Screen.BrandProducts.createRoute(brand = brand.id.toString()),
                            )
                        },
                    )
            }

    }
}

