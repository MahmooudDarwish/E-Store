package com.example.e_store.utils.shared_components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_store.R

@Composable
fun BackButton(onBackClick: () -> Unit) {
    Box (modifier = Modifier.padding(top = 20.dp))
    {
        Image(
            painter = painterResource(id = R.drawable.back_button),
            modifier = Modifier
                .height(50.dp)
                .width(25.dp)
                .wrapContentWidth(Alignment.Start)
                .clickable {
                    onBackClick()
                },
            contentDescription = "back",
            contentScale = ContentScale.Fit
        )
    }
}

