package com.example.e_store.features.location.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.e_store.R
import com.example.e_store.features.location.map.view_model.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.launch

@Composable
fun MapScreenFABs(
    viewModel: MapViewModel,
    fusedLocationClient: FusedLocationProviderClient,
    cameraPositionState: CameraPositionState,
    markerState: MarkerState,
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onClick: () -> Unit
    ) {
    val context = LocalContext.current




    Column(modifier = modifier) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            FloatingActionButton(
                onClick = {
                   onClick()

                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gps),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Location Save FAB
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.getAddressDetails(cameraPositionState.position.target, phoneNumber)
                    Toast.makeText(context, "Location Saved", Toast.LENGTH_SHORT).show()
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save_location),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            )
        }
    }
}
