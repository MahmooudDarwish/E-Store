package com.example.e_store.features.categories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.e_store.R
import com.example.e_store.features.home.component.SearchBox
import com.example.e_store.utils.shared_components.CustomIconButton
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_components.Gap


@Composable
fun SearchWithFavoriteWithFilterSection(
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ElevationCard(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            SearchBox(onClick = onSearchClick)
        }
        Gap(width = 8)

        ElevationCard {
            CustomIconButton(
                onClick = { onFavoriteClick() },
                icon = Icons.Default.FavoriteBorder
            )
        }

        Gap(width = 8)

        ElevationCard {
            CustomIconButton(
                onClick = { onFilterClick() },
                icon = ImageVector.vectorResource(id = R.drawable.ic_filter)
            )
        }
    }
}



