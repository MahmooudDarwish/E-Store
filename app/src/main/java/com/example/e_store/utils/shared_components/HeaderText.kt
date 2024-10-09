package com.example.e_store.utils.shared_components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.e_store.ui.theme.PrimaryColor

@Composable
fun HeaderText(headerText: String) {
    Text(
        modifier = Modifier
            .padding(top = 20.dp),
        text = headerText,
        style = MaterialTheme.typography.headlineLarge,
        color = PrimaryColor,
        textAlign = TextAlign.Center
    )
}