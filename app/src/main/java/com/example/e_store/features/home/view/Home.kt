package com.example.e_store.features.home.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_store.features.home.component.BrandsSection
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.features.home.component.SearchWithFavoriteSection
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.utils.shared_models.DataState

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val brandsUiState by viewModel.brands.collectAsStateWithLifecycle()
    val forUProducts by viewModel.brands.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getBrands()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SearchWithFavoriteSection(
            onSearchClick = {
                ///TODO: Navigation to search screen  @kk98989898
            },
            onFavoriteClick = {
                ///TODO: Navigation to Favorite screen @kk98989898
            },
        )


        Text(text = "Brands")
        when (brandsUiState) {
            DataState.Loading -> {
                EShopLoadingIndicator()
            }

            is DataState.Success -> {
                val brands = (brandsUiState as DataState.Success).data
                BrandsSection(onBrandClick = {}, brands = brands)
            }

            is DataState.Error -> {
                val errorMsg = (brandsUiState as DataState.Error).message
                LaunchedEffect(errorMsg) {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
        Text(text = "For you")



    }
}