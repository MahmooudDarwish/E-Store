package com.example.e_store.features.brand_products.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.brand_products.components.BrandProductsHeader
import com.example.e_store.utils.shared_components.ProductsStateHandler
import com.example.e_store.utils.shared_components.PriceSlider
import com.example.e_store.features.brand_products.view_model.BrandProductsViewModel
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_view_model.FavouriteControllerViewModel

@Composable
fun BrandProducts(
    brandID: String?,
    navController: NavHostController,
    viewModel: BrandProductsViewModel,
    favouriteControllerViewModel: FavouriteControllerViewModel
) {
    val brandProductsUiState by viewModel.brandProducts.collectAsStateWithLifecycle()
    var sliderMaxValue by remember { mutableFloatStateOf(1000F) }
    var maxPrice by remember { mutableFloatStateOf(0f) }
    var showSliderDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getBrandProducts(brandID = brandID!!)
    }

    LaunchedEffect(brandProductsUiState) {
        if (brandProductsUiState is DataState.Success) {
            val brandProducts = (brandProductsUiState as DataState.Success).data
            sliderMaxValue = brandProducts.maxOfOrNull { it.variants[0].price.toFloat() } ?: 10000f
            maxPrice = sliderMaxValue
        }
    }

    fun filterProducts(products: List<Product>): List<Product> {
        return products.filter { it.variants[0].price.toFloat() <= maxPrice }
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
            brandName = if (brandProductsUiState is DataState.Success) (brandProductsUiState as DataState.Success).data[0].vendor else null,
            onFilterClick = { showSliderDialog = !showSliderDialog }
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
            productsUiState = brandProductsUiState,
            navController = navController,
            route = NavigationKeys.PRODUCT_INFO_HOME_ROUTE,   /// TODO: add route to product info @MahmoudDariwsh @kk98989898
            filterProducts = ::filterProducts,
            viewModel = favouriteControllerViewModel
        )
    }
}
