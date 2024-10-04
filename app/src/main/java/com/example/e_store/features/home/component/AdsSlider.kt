package com.example.e_store.features.home.component

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlin.math.absoluteValue
import androidx.compose.foundation.clickable

@Composable
fun AdsSlider(
    images: List<SliderItem>,
    onItemClick: (SliderItem) -> Unit // Add a click callback
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(), // Ensures full-width pager
        pageSpacing = 0.dp, // No spacing between pages to ensure only one card per swipe
        pageSize = PageSize.Fill, // Makes each page fill the width of the screen
        snapPosition = SnapPosition.Center, // Snaps the page at the center
    ) { page ->
        val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                ).absoluteValue

        Card(
            modifier = Modifier
                .fillMaxSize() // Makes the card fill the available space
                .graphicsLayer {
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                }
                .clickable {
                    // Handle click on slide
                    onItemClick(images[page])
                }, // Make slide clickable
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            // Load the image into the card
            AsyncImage(
                imageLoader = gifEnabledLoader(LocalContext.current), // Pass context to the loader
                model = images[page].image,
                contentDescription = images[page].title,
                contentScale = ContentScale.Crop, // Adjust the scaling of the image
                modifier = Modifier.fillMaxSize(), // Ensures the image fills the card
            )
        }
    }
}

@Composable
fun gifEnabledLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
}

data class SliderItem(
    val image: Int,
    val title: String,
)
