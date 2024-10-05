package com.example.e_store.features.categories.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.e_store.R
import com.example.e_store.utils.constants.Keys

@Composable
fun FloatingActionButtonSection(
    fabExpanded: Boolean,
    onFabClick: () -> Unit,
    onProductTypeSelect: (String) -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (fabExpanded) 360f else 0f,
        animationSpec = tween(durationMillis = 500), label = "FAB-Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = fabExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            FloatingActionButton(
                onClick = {
                    onProductTypeSelect(Keys.SHOES_KEY)
                },

                ) {

                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_shoes),
                    contentDescription = "Expand FABs"
                )
            }
        }

        AnimatedVisibility(
            visible = fabExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            FloatingActionButton(
                onClick = {
                    onProductTypeSelect(Keys.ACCESSORIES_KEY)

                },

                ) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_glasses),
                    contentDescription = "Expand FABs"
                )
            }
        }

        AnimatedVisibility(
            visible = fabExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            FloatingActionButton(
                onClick = {
                    onProductTypeSelect(Keys.T_SHIRTS_KEY)
                },

                ) {
                Icon(
                    ImageVector.vectorResource(id = R.drawable.ic_tshirt),
                    contentDescription = "Expand FABs"
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier.graphicsLayer { rotationZ = rotationAngle },
            onClick = onFabClick
        ) {
            Icon(
                if (fabExpanded) Icons.Default.Close else ImageVector.vectorResource(id = R.drawable.ic_laundary),
                contentDescription = "Expand FABs"
            )
        }
    }
}
