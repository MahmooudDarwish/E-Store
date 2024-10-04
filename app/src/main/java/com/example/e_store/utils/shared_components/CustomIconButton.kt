package com.example.e_store.utils.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.e_store.ui.theme.PrimaryColor

@Composable
fun CustomIconButton(onClick: () -> Unit , icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clickable { onClick() }
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Favorite Icon",
            tint = PrimaryColor
        )
    }
}
