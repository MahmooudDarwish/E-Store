package com.example.e_store.features.home.component

import android.content.ClipboardManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.delay

@Composable
fun AdsSlider(
    initialImages: List<SliderItem>, // Receive the initial list of images
    onItemClick: (SliderItem) -> Unit,
) {
    // Initialize images using remember with apply to avoid multiple recompositions
    val images by rememberUpdatedState(newValue = initialImages)

    // Pager state for controlling the current page
    val pagerState = rememberPagerState(pageCount = { images.size })

    // Get the current context
    val context = LocalContext.current

    // Auto-slide effect that only runs when images are present
    LaunchedEffect(images.isNotEmpty()) {
        if (images.isNotEmpty()) {
            while (true) {
                delay(3000L) // Auto-slide every 3 seconds
                val nextPage = (pagerState.currentPage + 1) % images.size
                pagerState.animateScrollToPage(nextPage)
                Log.d("AdsSlider", "Auto sliding to page: $nextPage")
            }
        }
    }

    // Horizontal pager for image sliding
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        pageSpacing = 0.dp,
        pageSize = PageSize.Fill,
        snapPosition = SnapPosition.Center
    ) { page ->
        val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                ).absoluteValue

        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .clickable {
                    onItemClick(images[page])
                    copyToClipboard(context, images[page].title)
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
        ) {
            AsyncImage(
                imageLoader = gifEnabledLoader(context),
                model = images[page].image,
                contentDescription = images[page].title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxSize(),
            )
        }
    }
}

// Helper function to update slider images from outside
fun updateSliderImages(images: SnapshotStateList<SliderItem>, newImages: List<SliderItem>) {
    Log.d("updateSliderImages", "newImages: $newImages")
    Log.d("updateSliderImages", "images: $images")
    images.addAll(newImages)
}

// Custom ImageLoader to handle GIFs
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

// Data class for Slider items
data class SliderItem(
    val image: Int,
    val title: String,
)

// Function to copy text to clipboard and show a toast message
private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = android.content.ClipData.newPlainText("Coupons", text)
    clipboard.setPrimaryClip(clip)

}
