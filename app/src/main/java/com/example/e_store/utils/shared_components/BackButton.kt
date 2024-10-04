package com.example.e_store.utils.shared_components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_store.R

@Composable
fun BackButton(onBackClick: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.ic_back),
        modifier = Modifier
            .size(80.dp)
            .padding(top = 20.dp)
            .clickable {
                onBackClick()
            },
        contentDescription = "back",
        tint = Color.Unspecified
    )
}

