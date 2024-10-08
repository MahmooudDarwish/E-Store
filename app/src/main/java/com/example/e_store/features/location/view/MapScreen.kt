package com.example.e_store.features.location.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e_store.BuildConfig
import com.example.e_store.R
import com.example.e_store.features.location.view.components.SearchPlacesHeader
import com.example.e_store.features.search.components.SearchProductsHeader
import com.example.e_store.utils.shared_components.PriceSlider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val context = LocalContext.current

    // Initialize Places API
    Places.initialize(context, BuildConfig.GOOGLE_MAPS_API_KEY)
    val placesClient: PlacesClient = Places.createClient(context)
    var query by remember { mutableStateOf("") }

    // Intent launcher for Google Autocomplete
    val autocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val place = Autocomplete.getPlaceFromIntent(data)
                query = place.name ?: ""
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            val status: Status = Autocomplete.getStatusFromIntent(result.data)
            println("Autocomplete canceled: $status")
        }
    }


    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var currentLocation by remember { mutableStateOf<Location?>(null) }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var showPermissionDialog by remember { mutableStateOf(false) }
    var markerState = rememberMarkerState(position = LatLng(0.0, 0.0))

    // Google Map state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(0.0, 0.0), 10f, 0f, 0f)
    }


    // Request permissions
    LaunchedEffect(Unit) {
        locationPermissionsState.launchMultiplePermissionRequest()
    }

    // Check if permissions are granted and location services are enabled
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            // Check if GPS is enabled
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getCurrentLocation(fusedLocationClient) { location ->
                    currentLocation = location
                    Log.d("MapScreen", "Current Location: $location")

                    cameraPositionState.position = CameraPosition(
                        LatLng(
                            currentLocation!!.latitude,
                            currentLocation!!.longitude
                        ),
                        10f,
                        0f,
                        0f
                    )
                    markerState.position =
                        LatLng(currentLocation!!.latitude, currentLocation!!.longitude)

                }
            } else {
                showPermissionDialog = true
            }
        } else {
            showPermissionDialog = true
        }
    }



    Scaffold(
        topBar = {
            // UI for the search input
            Column(
                modifier = Modifier
                    .background(Color.White)
            ) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Enter a place") },
                    modifier = Modifier.fillMaxWidth()
                        .background( Color.White)
                )
                    val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, listOf(
                            Place.Field.ID, Place.Field.NAME
                        )
                    )

                        .build(context)
                    autocompleteLauncher.launch(intent)


            }

        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                FloatingActionButton(
                    onClick = {
                        if (locationPermissionsState.allPermissionsGranted) {
                            getCurrentLocation(fusedLocationClient) { location ->
                                currentLocation = location
                                Log.d("MapScreen", "Current Location: $location")
                                currentLocation?.let {
                                    cameraPositionState.position = CameraPosition(
                                        LatLng(
                                            it.latitude,
                                            it.longitude
                                        ), // Move to the new position
                                        10f, // Zoom level
                                        0f,
                                        0f
                                    )
                                }
                                markerState.position =
                                    LatLng(currentLocation!!.latitude, currentLocation!!.longitude)

                            }
                        } else {
                            showPermissionDialog = true
                        }
                    },
                ) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_gps),
                        contentDescription = "Get Current Location",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                ),
                onMapClick = { latLng ->
                    // Update currentLocation
                    currentLocation = Location("").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                    }
                    markerState.position =
                        LatLng(currentLocation!!.latitude, currentLocation!!.longitude)

                    cameraPositionState.position = CameraPosition(
                        LatLng(
                            currentLocation!!.latitude,
                            currentLocation!!.longitude
                        ), // Move to the new position
                        10f, // Zoom level
                        0f,
                        0f
                    )
                    Log.d("MapScreen", "Clicked Location: $latLng")
                    Log.d("MapScreen", "currentLocation: $currentLocation")
                    Log.d(
                        "MapScreen",
                        "Marker Location: ${markerState.position.latitude}, ${markerState.position.longitude}"
                    )
                }
            ) {
                // Only display marker if currentLocation is not null
                currentLocation?.let { location ->
                    Log.d(
                        "MapScreen",
                        "Marker Location: ${location.latitude}, ${location.longitude}"
                    )
                    Marker(
                        state = markerState,
                        title = "Your Location",
                        snippet = "Current Location"
                    )
                }
            }

        }
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Location Permission Required") },
            text = { Text("This app requires location permissions to function correctly. Please enable them in settings.") },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    openGPSSettings(context)
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationResult: (Location?) -> Unit
) {
    val locationTask = fusedLocationClient.lastLocation
    locationTask.addOnSuccessListener { location ->
        if (location != null) {
            onLocationResult(location)
        } else {
            onLocationResult(null) // No location available
        }
    }.addOnFailureListener { exception ->
        Log.e("MapScreen", "Error fetching location: ${exception.message}")
        onLocationResult(null) // If there's an error, return null
    }
}

// Function to open GPS settings
private fun openGPSSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    MapScreen()
}
