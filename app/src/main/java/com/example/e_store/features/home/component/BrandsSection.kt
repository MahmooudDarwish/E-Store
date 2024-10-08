package com.example.e_store.features.home.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.CircularItem
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.delay

@Composable
fun BrandsSection(navController: NavHostController, brands: List<Brand>) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { -it })
    ) {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            itemsIndexed(brands) { _, brand ->
                Log.d("brands", brands.size.toString())



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

}
