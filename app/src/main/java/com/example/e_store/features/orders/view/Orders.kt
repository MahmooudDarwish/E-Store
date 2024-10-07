package com.example.e_store.features.orders.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.features.orders.components.OrderItem
import com.example.e_store.features.orders.components.OrdersHeader
import com.example.e_store.features.orders.view_model.OrdersViewModel
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_models.DataState


@Composable
fun OrdersScreen(viewModel: OrdersViewModel, navController: NavHostController) {
    val ordersUiState by viewModel.orders.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getAllOrders()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 24.sp,
            color = PrimaryColor,
            fontStyle = FontStyle.Italic
        )
        OrdersHeader(
            navController = navController,
        )

        when (ordersUiState) {
            DataState.Loading -> {
                EShopLoadingIndicator()
            }

            is DataState.Success -> {
                val orders = (ordersUiState as DataState.Success).data
                LazyColumn {
                    if (orders.isEmpty()) {
                        item { Text(text = "No Orders yet") }
                    } else {
                        items(orders.size) { index ->

                            OrderItem(order = orders[index])
                        }
                    }

                }
            }

            is DataState.Error -> {
                val errorMsg = (ordersUiState as DataState.Error).message
                val context = LocalContext.current
                LaunchedEffect(errorMsg) {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}