package com.example.e_store.utils.shared_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_methods.convertCurrency
import kotlin.math.ceil

@Composable
fun PriceSlider(
    maxPrice: Float,
    sliderMaxValue: Float,
    onPriceChange: (Float) -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
            modifier = Modifier.padding(horizontal = 40.dp),
            value = maxPrice,
            onValueChange = onPriceChange,
            valueRange = 0f..sliderMaxValue,
            colors = SliderDefaults.colors(
                thumbColor = PrimaryColor,
                activeTrackColor = PrimaryColor,
            )
        )
        Text(
            text = "Max Price: ${ convertCurrency(ceil(maxPrice.toDouble()))} ",        ///todo change currency
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            textAlign = TextAlign.Start,
            color = Color.Gray
        )
    }
}
