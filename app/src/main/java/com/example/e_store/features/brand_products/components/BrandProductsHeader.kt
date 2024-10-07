package com.example.e_store.features.brand_products.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.CustomIconButton
import com.example.e_store.utils.shared_components.ElevationCard


@Composable
fun BrandProductsHeader(
    navController: NavHostController,
    brandName: String?,
    onFilterClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBackClick = { navController.popBackStack() })


        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = brandName ?: "Brands",
            style = MaterialTheme.typography.titleMedium
        )


        ElevationCard(modifier = Modifier.padding(top = 20.dp)) {
            CustomIconButton(
                onClick = onFilterClick,
                icon = ImageVector.vectorResource(id = R.drawable.ic_filter)
            )
        }
    }
}




