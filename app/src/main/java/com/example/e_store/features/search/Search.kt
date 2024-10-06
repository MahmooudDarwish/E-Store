package com.example.e_store.features.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.brand_products.components.BrandProductsHeader
import com.example.e_store.features.search.view_model.SearchViewModel
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_components.PriceSlider
import com.example.e_store.utils.shared_components.ProductsStateHandler
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product

@Composable
fun SearchScreen(viewModel: SearchViewModel , navController: NavHostController) {


    val productsUiState by viewModel.products.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var sliderMaxValue by remember { mutableFloatStateOf(1000F) }
    var maxPrice by remember { mutableFloatStateOf(0f) }
    var showSliderDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchAllProducts()
    }

    LaunchedEffect(productsUiState) {

        if (productsUiState is DataState.Success) {
            val products = (productsUiState as DataState.Success).data
            sliderMaxValue = products.maxOfOrNull { it.variants[0].price.toFloat() } ?: 10000f
            maxPrice = sliderMaxValue
        }
    }
    fun filterProducts(products: List<Product>): List<Product> {
        return products.filter { it.variants[0].price.toFloat() <= maxPrice &&
                (searchQuery.isEmpty() || it.title.startsWith(searchQuery, ignoreCase = true))
        }
    }



    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = context.getString(R.string.app_name),
            fontSize = 24.sp,
            color = PrimaryColor,
            fontStyle = FontStyle.Italic
        )
        BrandProductsHeader(
            navController = navController,
            brandName = if (productsUiState is DataState.Success) (productsUiState as DataState.Success).data[0].vendor else null,
            onFilterClick = { showSliderDialog = !showSliderDialog },
            isSearchEnabled = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )

        if (showSliderDialog) {
            PriceSlider(
                maxPrice = maxPrice,
                sliderMaxValue = sliderMaxValue,
                onPriceChange = { maxPrice = it }
            )
        }

        Gap(height = 20)

        ProductsStateHandler(
            productsUiState = productsUiState,
            route = NavigationKeys.PRODUCT_INFO_HOME_ROUTE,
            navController = navController,
            filterProducts = ::filterProducts
        )

    }
}





