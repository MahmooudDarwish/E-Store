package com.example.e_store.features.orders.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.Gap


@Composable
fun OrdersHeader(
    navController: NavHostController,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBackClick = { navController.popBackStack() })
        Gap(width = 35)

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "Orders",
            style = MaterialTheme.typography.titleMedium
        )

    }
}




