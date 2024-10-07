package com.example.e_store.features.search.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.CustomIconButton
import com.example.e_store.utils.shared_components.ElevationCard

@Composable
fun SearchProductsHeader(
    navController: NavHostController,
    onFilterClick: () -> Unit,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBackClick = { navController.popBackStack() })

        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth(.8f)
                .height(70.dp)
                .padding(top = 20.dp, end = 6.dp , start = 4.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
        )

        ElevationCard(modifier = Modifier.padding(top = 20.dp)) {
            CustomIconButton(
                onClick = onFilterClick,
                icon = ImageVector.vectorResource(id = R.drawable.ic_filter)
            )
        }
    }
}
