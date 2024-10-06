package com.example.e_store.features.search.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
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
import com.example.e_store.utils.shared_methods.initializeProductDetails
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.shadow

@Composable
fun SearchScreen(viewModel: SearchViewModel, navController: NavHostController) {
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
        return products.filter {
            it.variants[0].price.toFloat() <= maxPrice &&
                    (searchQuery.isEmpty() || it.title.startsWith(searchQuery, ignoreCase = true))
        }
    }

    val filteredSuggestions = if (productsUiState is DataState.Success) {
        (productsUiState as DataState.Success).data.filter {
            searchQuery.isNotEmpty() && it.title.startsWith(searchQuery, ignoreCase = true)
        }
    } else emptyList()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = context.getString(R.string.app_name),
                fontSize = 24.sp,
                color = PrimaryColor,
                fontStyle = FontStyle.Italic
            )

            BrandProductsHeader(
                navController = navController,
                brandName = if (productsUiState is DataState.Success)
                    (productsUiState as DataState.Success).data[0].vendor else null,
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

        AnimatedVisibility(
            visible = searchQuery.isNotEmpty() && filteredSuggestions.isNotEmpty(),
            enter = fadeIn() + expandIn() + slideInVertically(),
            exit = fadeOut() + shrinkOut() + slideOutVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 105.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .heightIn(max = 300.dp)
                    .shadow(elevation = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                ) {
                    items(filteredSuggestions) { suggestion ->
                        SuggestionItem(
                            suggestion = suggestion,
                            onSuggestionClick = {
                                searchQuery = suggestion.title
                                initializeProductDetails(suggestion)
                                navController.navigate(NavigationKeys.PRODUCT_INFO_HOME_ROUTE)
                            }
                        )
                    }
                }
            }
        }
    }


}
@Composable
fun SuggestionItem(
    suggestion: Product,
    onSuggestionClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSuggestionClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = suggestion.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = suggestion.vendor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}






