package com.example.e_store.features.categories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.example.e_store.R

@Composable
fun CategoryList(onCategorySelect: (String) -> Unit) {

    val context = LocalContext.current

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val categories = listOf(
            R.string.category_men,
            R.string.category_women,
            R.string.category_kid,
            R.string.category_sale
        )

        items(categories.size) { category ->
            CategoryCard(category = getString(context, categories[category])) {
                onCategorySelect(getString(context, categories[category]))
            }
        }
    }
}
