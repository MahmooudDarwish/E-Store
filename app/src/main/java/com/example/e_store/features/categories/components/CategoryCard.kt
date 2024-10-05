package com.example.e_store.features.categories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_store.utils.shared_components.ElevationCard

@Composable
fun CategoryCard(category: String, onClick: () -> Unit) {
    ElevationCard(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .clickable { onClick() }
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category, fontSize = 16.sp, textAlign = TextAlign.Center
            )
        }
    }
}