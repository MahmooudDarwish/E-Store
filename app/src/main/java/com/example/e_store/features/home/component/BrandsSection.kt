package com.example.e_store.features.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.e_store.utils.shared_components.CircularItem
import com.example.e_store.utils.shared_models.Brand

@Composable
fun BrandsSection(onBrandClick: (Brand) -> Unit, brands: List<Brand>?) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            brands?.let {
                items(it.size) { current ->
                    CircularItem(
                        title = brands[current].title,
                        imageUrl = brands[current].image!!.src,
                        onClick = { onBrandClick(brands[current]) }
                    )
                }
            }
        }
}





/*
@Composable
fun ProductCard(productName: String, price: String, iconResId: Int) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = productName,
                modifier = Modifier.size(80.dp)
            )
            Text(text = productName, style = MaterialTheme.typography.body1)
            Text(text = price, style = MaterialTheme.typography.body2)
        }
    }
}*/