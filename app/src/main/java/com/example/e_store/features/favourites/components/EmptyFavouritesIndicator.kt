package com.example.e_store.features.favourites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.e_store.ui.theme.PrimaryColor

@Composable
fun EmptyFavouritesIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Empty Favourites",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp),
            tint = PrimaryColor
        )
        Text(
            text = "Your favourites list is empty",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = PrimaryColor
        )
        Text(
            text = "Add items to your favourites to see them here",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp),
            color = PrimaryColor
        )
    }
}