package com.example.e_store.features.location.components

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import com.example.e_store.utils.shared_methods.PermissionUtil
import com.example.e_store.utils.shared_methods.PermissionUtil.requestLocationPermissions

@Composable
fun GpsPreemptionScreen(
    onGpsAndPermissionStatusChange: (isGpsEnabled: Boolean, isPermissionGranted: Boolean) -> Unit
) {

    var isPermissionGranted by remember { mutableStateOf(false) }
    var isGpsEnabled by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showGpsDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity

    // Check permissions
    LaunchedEffect(Unit) {
        isPermissionGranted = PermissionUtil.checkLocationPermissions(context)
        isGpsEnabled = PermissionUtil.isGpsEnabled(context)

        onGpsAndPermissionStatusChange(isGpsEnabled, isPermissionGranted)

        if (!isPermissionGranted) {
            Log.d("GpsPreemptionScreen", "Requesting location permissions")
            showPermissionDialog = true
        }
        if (!isGpsEnabled) {
            Log.d("GpsPreemptionScreen", "GPS is not enabled")
            showGpsDialog = true
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("GPS Preemption Check") })
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                if (!isPermissionGranted) {
                    Text("Location permission is required.")
                }
                if (!isGpsEnabled) {
                    Text("GPS is not enabled.")
                }

                if (isPermissionGranted && isGpsEnabled) {
                    Text("GPS is enabled and permission granted.")
                }
            }
        }
    )

    // Permission Dialog
    if (showPermissionDialog) {
        PermissionDialog(
            onDismiss = { showPermissionDialog = false },
            onConfirm = {
                activity?.let { requestLocationPermissions(it) }
                showPermissionDialog = false
            }
        )
    }

    // GPS Dialog
    if (showGpsDialog) {
        GpsDialog(
            onDismiss = { showGpsDialog = false },
            onConfirm = {
                // Redirect to location settings
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
                showGpsDialog = false
            }
        )
    }
}

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Location Permission Required") },
        text = { Text("Please grant location permission to use this feature.") },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onConfirm()
            }) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@Composable
fun GpsDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("GPS Required") },
        text = { Text("Please enable GPS to continue using this feature.") },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onConfirm()
            }) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

