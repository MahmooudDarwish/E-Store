package com.example.e_store.features.home.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun AdsSlider(images: List<String>) {


    Card(
        modifier = Modifier
            .fillMaxHeight(),

        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    ) {
        AsyncImage(
            model = images[0],
            contentDescription = images[0],
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
