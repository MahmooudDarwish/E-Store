package com.example.e_store.features.favourites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_components.HeaderText

@Composable
fun FavouritesHeader(navController: NavHostController) {

    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBackClick = { navController.popBackStack() })

        Gap(width = 20)
        HeaderText(headerText = stringResource(id = R.string.favourite))
    }

}