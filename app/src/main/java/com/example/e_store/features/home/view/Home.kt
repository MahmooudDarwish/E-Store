package com.example.e_store.features.home.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.home.component.AdsSlider
import com.example.e_store.features.home.component.BrandsSection
import com.example.e_store.features.home.component.ForUSection
import com.example.e_store.features.home.component.SearchWithFavoriteSection
import com.example.e_store.features.home.component.SliderItem
import com.example.e_store.features.home.component.updateSliderImages
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_models.DataState
import androidx.compose.ui.res.stringResource
import com.example.e_store.utils.shared_components.Popup
import com.example.e_store.utils.shared_models.UserSession

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavHostController) {
    val context = LocalContext.current

    val brandsUiState by viewModel.brands.collectAsStateWithLifecycle()
    val forUProductsUiState by viewModel.forUProducts.collectAsStateWithLifecycle()
    val discountCodesUiState by viewModel.discountCodes.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()


    if (!UserSession.isGuest) {

        LaunchedEffect(Unit) {
            UserSession.email?.let { viewModel.fetchShopifyCustomer(it) }
        }

        Log.i("HomeScreen", "HomeScreen: ${UserSession.shopifyCustomerID}")
    }


    var showLoginDialog by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshAllData() }
    )

    val sliderImages = remember {
        mutableStateListOf<SliderItem>().apply {
            addAll(
                listOf(
                    SliderItem(R.drawable.addsgifone, "New Collection of Shirts"),
                    SliderItem(R.drawable.adsgiftwo, "New Collection of Shoes"),
                    SliderItem(R.drawable.adsgifthree, "New Collection of Hoodies"),
                    SliderItem(R.drawable.adsgiffour, "New Collection of Shirts"),
                    SliderItem(R.drawable.adsgiffive, "New Collection of Hoodies"),
                    SliderItem(R.drawable.adsgifsix, "New Collection of Shoes")
                )
            )
        }
    }

    when (discountCodesUiState) {
        DataState.Loading -> {
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            (discountCodesUiState as DataState.Success).data?.let { data ->

               val newSliderItems = data.discount_codes.map { discountCode ->
                    val codeLower = discountCode.code.lowercase()
                    val imageId = when (codeLower) {
                        "fivepercentoff" -> R.drawable.fivepercentoff
                        "tenpercentoff" -> R.drawable.tenpercentoff
                        "fifteenpercentoff" -> R.drawable.fifteenpercentoff
                        "twentypercentoff" -> R.drawable.twentypercentoff
                        "twentyfivepercentoff" -> R.drawable.twentyfivepercentoff
                        "thirtypercentoff" -> R.drawable.thirtypercentoff
                        "freeship" -> R.drawable.freeship
                        else -> 0
                    }
                    SliderItem(imageId, discountCode.code)
                }
                Log.d("HomeScreen", "newSliderItems: $newSliderItems")
                updateSliderImages(sliderImages, newSliderItems)
            }

        }

        is DataState.Error -> {
            val errorMsg = (discountCodesUiState as DataState.Error).message
            Log.e("HomeScreen", "Error fetching discount codes: $errorMsg")
            LaunchedEffect(errorMsg) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshAllData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(color = Color.White)
    ) {

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SearchWithFavoriteSection(
                onSearchClick = {
                    navController.navigate(Screen.SearchFromHome.route)
                },
                onFavoriteClick = {
                     if (UserSession.isGuest) {
                        showLoginDialog = true
                    } else {
                        navController.navigate(Screen.FavouriteFromHome.route)
                    }
                }
            )


            AdsSlider(initialImages = sliderImages) { clickedItem ->
                Toast.makeText(
                    context,
                    "Coupons: ${clickedItem.title} Copied!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            Gap(height = 16)
                when (brandsUiState) {
                    DataState.Loading -> EShopLoadingIndicator()

                    is DataState.Success -> {
                        val brands = (brandsUiState as DataState.Success).data

                        Text(
                            text = stringResource(id = R.string.brands),
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                            fontSize = 20.sp
                        )
                        BrandsSection(navController = navController, brands = brands)
                    }

                    is DataState.Error -> {
                        val errorMsg = (brandsUiState as DataState.Error).message
                        LaunchedEffect(errorMsg) {
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }



                when (forUProductsUiState) {
                    DataState.Loading -> {} //EShopLoadingIndicator()

                    is DataState.Success -> {
                        val forUProducts = (forUProductsUiState as DataState.Success).data
                        Text(
                            text = stringResource(id = R.string.for_you),
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                            fontSize = 20.sp
                        )
                        Gap(height = 16)
                        ForUSection(
                            navController = navController,
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



        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}
