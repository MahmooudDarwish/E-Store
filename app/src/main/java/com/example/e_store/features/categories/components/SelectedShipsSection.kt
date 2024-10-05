package com.example.e_store.features.categories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectedChipsSection(
    selectedProductType: String,
    selectedCategory: String,
    onProductTypeClear: () -> Unit,
    onCategoryClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        if (selectedProductType.isNotEmpty()) {
            SelectableChip(label = selectedProductType) { onProductTypeClear() }
        }

        if (selectedCategory.isNotEmpty()) {
            SelectableChip(label = selectedCategory) { onCategoryClear() }
        }
    }
}
