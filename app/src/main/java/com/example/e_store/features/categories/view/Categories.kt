package com.example.e_store.features.categories.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.e_store.features.categories.components.CategoryList
import com.example.e_store.features.categories.components.FloatingActionButtonSection
import com.example.e_store.features.categories.components.SearchWithFavoriteWithFilterSection
import com.example.e_store.features.categories.components.SelectedChipsSection
import com.example.e_store.utils.shared_components.ProductsStateHandler
import com.example.e_store.features.categories.view_model.CategoriesViewModel
import com.example.e_store.utils.constants.Keys
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.PriceSlider
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product

@Composable
fun CategoriesScreen(viewModel: CategoriesViewModel, navController: NavHostController) {
    val categoriesProductsUiState by viewModel.categoriesProducts.collectAsStateWithLifecycle()

    var sliderMaxValue by remember { mutableFloatStateOf(1000F) }
    var maxPrice by remember { mutableFloatStateOf(0f) }
    var showSliderDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedProductType by remember { mutableStateOf("") }
    var fabExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCategoriesProducts()
    }

    LaunchedEffect(categoriesProductsUiState) {
        if (categoriesProductsUiState is DataState.Success) {
            val brandProducts = (categoriesProductsUiState as DataState.Success).data
            sliderMaxValue = brandProducts.maxOfOrNull { it.variants[0].price.toFloat() } ?: 10000f
            maxPrice = sliderMaxValue
        }
    }

    fun filterProducts(products: List<Product>): List<Product> {
        return products.filter { product ->
            val categoryMatches = when (selectedCategory.lowercase()) {
                Keys.MEN_KEY -> product.tags?.contains(Keys.MEN_KEY) == true || product.tags?.contains(
                    Keys.UNISEX_KEY
                ) == true

                Keys.WOMEN_KEY -> product.tags?.contains(Keys.WOMEN_KEY) == true || product.tags?.contains(
                    Keys.UNISEX_KEY
                ) == true

                else -> selectedCategory.isEmpty() || product.tags?.contains(selectedCategory.lowercase()) == true
            }

            val productTypeMatches =
                selectedProductType.isEmpty() || product.productType == selectedProductType
            val priceMatches = product.variants[0].price.toFloat() <= maxPrice

            categoryMatches && productTypeMatches && priceMatches
        }
    }



    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .padding(0.dp),
        floatingActionButton = {
            FloatingActionButtonSection(
                fabExpanded = fabExpanded,
                onFabClick = { fabExpanded = !fabExpanded },
                onProductTypeSelect = { selectedProductType = it }
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(contentPadding)

        ) {
            item {
                SearchWithFavoriteWithFilterSection(
                    onSearchClick = {
                        /// TODO: navigate to Search screen @MahmoudDariwsh @kk98989898
                    },
                    onFavoriteClick = {
                        /// TODO: navigate to favorite screen @MahmoudDariwsh @kk98989898
                    },
                    onFilterClick = { showSliderDialog = !showSliderDialog },
                )
            }

            if (showSliderDialog) {
                item {
                    PriceSlider(maxPrice = maxPrice,
                        sliderMaxValue = sliderMaxValue,
                        onPriceChange = { maxPrice = it })
                }
            }
            item {
                CategoryList(
                    onCategorySelect = { selectedCategory = it })
            }

            item {
                SelectedChipsSection(
                    selectedProductType = selectedProductType,
                    selectedCategory = selectedCategory,
                    onProductTypeClear = { selectedProductType = "" },
                    onCategoryClear = { selectedCategory = "" }
                )
            }
            item {
                ProductsStateHandler(
                    productsUiState = categoriesProductsUiState,
                    navController = navController,
                    route = NavigationKeys.PRODUCT_INFO_CATEGORIES_ROUTE, /// TODO: add route to product info @MahmoudDariwsh @kk98989898
                    filterProducts = ::filterProducts
                )
            }
        }
    }
}





