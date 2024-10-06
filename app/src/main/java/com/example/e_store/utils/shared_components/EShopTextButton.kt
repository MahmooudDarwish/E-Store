package com.example.e_store.utils.shared_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import com.example.e_store.R

@Composable
fun EShopTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: Int = 16,
    color: androidx.compose.ui.graphics.Color = colorResource(id = R.color.colorPrimaryButton)
) {
    TextButton(
        onClick = onClick,
    ) {
        Text(text, color = color, fontSize = fontSize.sp)
    }
}
