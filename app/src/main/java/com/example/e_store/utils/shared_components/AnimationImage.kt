package com.example.e_store.utils.shared_components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimationImage(
    modifier: Modifier = Modifier,
    imageRes: Int,
    initialSize: Float = 150f,
    finalSize: Float = 200f,
    animationDuration: Int = 2000,
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(initialSize.dp) }

    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = animationDuration, easing = LinearEasing),
        label = ""
    )

    val animatedImageSize by animateDpAsState(
        targetValue = imageSize,
        animationSpec = tween(durationMillis = animationDuration),
        label = ""
    )

    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            rotationAngle = 360f
            imageSize = finalSize.dp
            delay(animationDuration.toLong())
            rotationAngle = 0f
            imageSize = initialSize.dp
            delay(animationDuration.toLong())
            isAnimating = false
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = CircleShape
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(animatedImageSize)
                .clip(CircleShape)
                .rotate(animatedRotationAngle)
        )
    }
}
