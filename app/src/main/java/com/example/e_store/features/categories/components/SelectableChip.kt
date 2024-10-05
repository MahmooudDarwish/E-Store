package com.example.e_store.features.categories.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.Gap

@Composable
fun SelectableChip(label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = PrimaryColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row {
            Text(
                text = label,
                modifier = Modifier.padding(8.dp),
                color = Color.Black
            )
            Gap(5)
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.padding(8.dp),
                tint = Color.Black
            )
        }
    }
}
