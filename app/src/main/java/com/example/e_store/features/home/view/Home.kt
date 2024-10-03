package com.example.e_store.features.home.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.e_store.features.home.component.BrandsSection
import com.example.e_store.features.home.component.ForUSection
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.features.home.component.SearchWithFavoriteSection
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.DataState

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val brandsUiState by viewModel.brands.collectAsStateWithLifecycle()
    val forUProductsUiState by viewModel.forUProducts.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getBrands()
        viewModel.getForUProducts()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp).background(color = Color.White)
    ) {
        item {
            SearchWithFavoriteSection(
                onSearchClick = {
                    ///TODO: Navigation to search screen  @kk98989898
                },
                onFavoriteClick = {
                    ///TODO: Navigation to Favorite screen @kk98989898
                },
            )
        }

        item {
            Text(text = "Brands", modifier = Modifier.padding(vertical = 8.dp))
        }

        item {
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
        }



        item {
            Text(text = "For you", modifier = Modifier.padding(vertical = 8.dp))
        }

        item {
            Gap(height = 16)
        }

        item {
            when (forUProductsUiState) {
                DataState.Loading -> {
                    EShopLoadingIndicator()
                }
                is DataState.Success -> {
                    val forUProducts = (forUProductsUiState as DataState.Success).data
                    ForUSection(
                        onProductClick = {
                            ///TODO: Navigation to product details screen  @MahmoudDarwish @kk98989898
                        },
                        products = forUProducts
                    )
                }
                is DataState.Error -> {
                    val errorMsg = (forUProductsUiState as DataState.Error).message
                    LaunchedEffect(errorMsg) {
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
