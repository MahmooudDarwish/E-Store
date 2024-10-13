package com.example.e_store.features.location.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.e_store.R
import com.example.e_store.features.location.components.SearchMapBox
import com.example.e_store.features.location.view_model.MapViewModel
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_models.UserAddress
import com.example.e_store.utils.shared_models.UserSession
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.example.e_store.BuildConfig
import java.util.Locale

private fun checkLocationPermissions(context: Context): Boolean {
    val fineLocationGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseLocationGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fineLocationGranted && coarseLocationGranted

}

private fun GPSEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    return gpsEnabled

}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

private fun openGPSSSettings(context: Context) {

    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable

fun MapScreen(navController: NavController, viewModel: MapViewModel) {
    val context = LocalContext.current

    val isPhoneExist by viewModel.isPhoneExist.collectAsState()


    var showPhoneNumberDialog by remember { mutableStateOf(false) }
    var inputValue by remember { mutableStateOf(UserSession.phone) }

    var isMapInitialized by remember { mutableStateOf(false) }


    Places.initialize(context, BuildConfig.GOOGLE_MAPS_API_KEY)
    var query by remember { mutableStateOf(context.getString(R.string.search)) }

    var currentLocation by remember { mutableStateOf<Location?>(null) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showGPSPermissionDialog by remember { mutableStateOf(false) }

    val markerState = rememberMarkerState(position = LatLng(0.0, 0.0))

    // Google Map state
    val startLocation = LatLng(40.9971, 29.1007)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 15f)
    }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )


    LaunchedEffect(Unit) {
        Log.d("MapScreen", "LaunchedEffect called")
        if (checkLocationPermissions(context)) {
            if (GPSEnabled(context)) {
                viewModel.fetchCustomerAddresses()

                Log.d("MapScreen", "Requesting permissions")
            } else {
                Log.d("MapScreen", "GPS is not enabled")
                showGPSPermissionDialog = true
            }
        } else {
            Log.d("MapScreen", "Permissions already granted")
            locationPermissionsState.launchMultiplePermissionRequest()

        }
    }




    PhoneNumberInputDialog(
        showDialog = showPhoneNumberDialog,
        onDismiss = { showPhoneNumberDialog = false },
        onConfirm = { phoneNumber ->
            inputValue = phoneNumber
            showPhoneNumberDialog = false

            currentLocation.let { location ->
                // Perform reverse geocoding to get address details
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses: List<Address>? = geocoder.getFromLocation(
                    location!!.latitude,
                    location.longitude,
                    1
                )

                addresses?.let {
                    val address = it.firstOrNull()
                    if (address != null) {
                        // Update the Adresse object
                        UserAddress.apply {
                            address1 = address.getAddressLine(0)
                            city = address.locality
                            province = address.adminArea
                            country = address.countryName
                            zip = address.postalCode
                            country_code = address.countryCode
                        }


                        Log.d("MapphoneNumber", "phoneNumber: ${UserSession.phone}")
                        Log.d(" MapphoneNumber", "phoneNumber: $inputValue")
                        viewModel.saveAddress(
                            com.example.e_store.utils.shared_models.Address(
                                address1 = UserAddress.address1,
                                city = UserAddress.city,
                                phone = inputValue,
                                first_name = UserSession.name,
                                country = UserAddress.country,
                                country_code = UserAddress.country_code


                            )
                        )

                        Log.d("PhoneNumberTag", "phoneNumber: ${UserSession.phone}")
                        Toast.makeText(
                            context,
                            "Location Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                        //navController.navigate(NavigationKeys.LOCATION_ROUTE)


                        Log.d(
                            "SaveLocation9512",
                            "Location saved: ${UserAddress.address1}"
                        )
                    } else {
                        Log.e(
                            "SaveLocation",
                            "No address found for the current location"
                        )
                    }
                } ?: run {
                    Log.e(
                        "SaveLocation",
                        "Unable to retrieve address for current location"
                    )
                }
            }
        },
        context
    )

    inputValue?.let { viewModel.isPhoneExistFUN(it) }


    // Intent launcher for Google Autocomplete
    val autocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                // Get the place from the autocomplete result
                val place = Autocomplete.getPlaceFromIntent(data)

                // Use the place's location (latitude and longitude)
                val latLng = place.latLng
                latLng?.let {
                    // Update marker position based on the selected place
                    markerState.position = LatLng(latLng.latitude, latLng.longitude)

                    // Optionally, move the camera to the new position
                    cameraPositionState.position = CameraPosition(
                        LatLng(latLng.latitude, latLng.longitude), // Move to the new position
                        10f, // Zoom level
                        0f,  // Tilt angle
                        0f   // Bearing
                    )
                    currentLocation = Location("").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude

                    }

                    // Update the query with the place's name
                    query = place.name ?: context.getString(R.string.search)
                }
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            val status: Status? = result.data?.let { Autocomplete.getStatusFromIntent(it) }
            println("Autocomplete canceled: $status")
        }
    }


    if (isMapInitialized) {
        // Check if permissions are granted and location services are enabled
        LaunchedEffect(
            checkLocationPermissions(context)
        ) {
            if (checkLocationPermissions(context)) {
                // Check if GPS is enabled
                if (GPSEnabled(context)) {
                    getCurrentLocation(fusedLocationClient, context, onNoPermission = {
                        showPermissionDialog = true
                    }) { location ->
                        currentLocation = location
                        Log.d("MapScreen", "Current Location: $location")

                        currentLocation.let {
                            cameraPositionState.position = CameraPosition(
                                LatLng(it!!.latitude, it.longitude),
                                10f,
                                0f,
                                0f
                            )
                            markerState.position = LatLng(it.latitude, it.longitude)
                        }

                    }
                } else {
                    Log.d("MapScreen", "GPS is not enabled11")
                    showGPSPermissionDialog = true
                }
            } else {
                Log.d("MapScreen", "Permissions not granted22")
                showPermissionDialog = true
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackButton {
                        navController.popBackStack()
                        navController.popBackStack()
                    }
                    ElevationCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        SearchMapBox(
                            query = query,
                            onClick = {
                                val intent = Autocomplete.IntentBuilder(
                                    AutocompleteActivityMode.FULLSCREEN,
                                    listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                                ).build(context)
                                autocompleteLauncher.launch(intent)
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            Column {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    FloatingActionButton(
                        onClick = {
                            Log.d("MapScreen", "Button clicked")
                            if (checkLocationPermissions(context)) {
                                if (GPSEnabled(context)) {
                                    Log.d("MapScreen", "Location permissions granted")
                                    getCurrentLocation(
                                        fusedLocationClient,
                                        context = context, onNoPermission = {
                                            showPermissionDialog = true
                                        }
                                    ) { location ->
                                        currentLocation = location
                                        Log.d("MapScreen", "Current Location: $location")
                                        currentLocation.let {
                                            cameraPositionState.position = CameraPosition(
                                                LatLng(
                                                    it!!.latitude,
                                                    it.longitude
                                                ), // Move to the new position
                                                10f, // Zoom level
                                                0f,
                                                0f
                                            )
                                            // Update marker position to the current location
                                            markerState.position = LatLng(it.latitude, it.longitude)
                                        }
                                        query = context.getString(R.string.search)
                                    }
                                } else {
                                    Log.d("MapScreen", "GPS is not enabled")
                                    showGPSPermissionDialog = true
                                }
                            } else {

                                showPermissionDialog = true
                                Log.d("MapScreen", "Location permissions not granted")
                            }
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_gps),
                            contentDescription = stringResource(R.string.get_current_location),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    FloatingActionButton(
                        onClick = {
                            currentLocation.let { location ->
                                // Perform reverse geocoding to get address details
                                val geocoder = Geocoder(context, Locale.getDefault())
                                val addresses: List<Address>? = geocoder.getFromLocation(
                                    location!!.latitude,
                                    location.longitude,
                                    1
                                )

                                addresses?.let {
                                    val address = it.firstOrNull()
                                    if (address != null) {
                                        // Update the Adresse object
                                        UserAddress.apply {
                                            address1 = address.getAddressLine(0)
                                            city = address.locality
                                            province = address.adminArea
                                            country = address.countryName
                                            zip = address.postalCode
                                        }

                                        Log.d("MapphoneNumber", "phoneNumber: ${isPhoneExist}")
                                        if (UserSession.phone == null || isPhoneExist) {
                                            showPhoneNumberDialog = true

                                        } else {
                                            Log.d(
                                                "MapphoneNumber",
                                                "phoneNumber: ${UserSession.phone}"
                                            )
                                            Log.d(" MapphoneNumber", "phoneNumber: ${inputValue}")
                                            viewModel.saveAddress(
                                                com.example.e_store.utils.shared_models.Address(
                                                    address1 = UserAddress.address1,
                                                    city = UserAddress.city,
                                                    phone = inputValue,
                                                    first_name = UserSession.name,
                                                )
                                            )
                                            Toast.makeText(
                                                context,
                                                "Location Saved",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            //navController.navigate(NavigationKeys.LOCATION_ROUTE)

                                        }
                                        Log.d(
                                            "SaveLocation",
                                            "Location saved25: ${UserAddress.address1}"
                                        )
                                    } else {
                                        Log.e(
                                            "SaveLocation",
                                            "No address found for the current location"
                                        )
                                    }
                                } ?: run {
                                    Log.e(
                                        "SaveLocation",
                                        "Unable to retrieve address for current location"
                                    )
                                }
                            }
                        },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_save_location),
                            contentDescription = stringResource(R.string.save_location),
                            modifier = Modifier.size(30.dp)
                        )

                    }

                }
            }
        }
    )
    { paddingValues ->

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
                onMapLoaded = {
                    // This callback triggers when the map has fully loaded
                    isMapInitialized = true
                },
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
                    query = context.getString(R.string.search)

                    Log.d("MapScreen", "Clicked Location: $latLng")
                    Log.d("MapScreen", "currentLocation: $currentLocation")
                    Log.d(
                        "MapScreen",
                        "Marker Location: ${markerState.position.latitude}, ${markerState.position.longitude}"
                    )
                }
            ) {
                // Only display marker if currentLocation is not null
                currentLocation.let { location ->
                    Marker(
                        state = markerState,
                        title = stringResource(R.string.your_location),
                        snippet = stringResource(R.string.current_location)
                    )
                }
            }

        }
    }
    if (showGPSPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showGPSPermissionDialog = false },
            title = { Text(stringResource(R.string.location_permission_required)) },
            text = { Text(stringResource(R.string.this_app_requires_location_permissions_to_function_correctly_please_enable_gps_in_settings)) },
            confirmButton = {
                TextButton(onClick = {
                    openGPSSSettings(context)
                    showGPSPermissionDialog = false
                }) {
                    Text(stringResource(R.string.open_settings))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text(stringResource(R.string.location_permission_required)) },
            text = { Text(stringResource(R.string.this_app_requires_location_permissions_to_function_correctly_please_enable_them_in_settings)) },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    openAppSettings(context)
                }) {
                    Text(stringResource(R.string.open_settings))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}


fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onNoPermission: () -> Unit,
    onLocationReceived: (Location) -> Unit,
) {


    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        onNoPermission()
        Toast.makeText(context, "Location Permission not granted", Toast.LENGTH_SHORT).show()
        return
    } else {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onLocationReceived(location ?: Location("").apply {
                    longitude = 0.0
                    latitude = 0.0
                })
            }
            .addOnFailureListener {
                Log.e("MapScreen", "Failed to retrieve location: ${it.message}")
            }
    }

}


// Function to open GPS settings


@Composable
fun PhoneNumberInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    context: Context,
) {
    var inputValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Phone number regex: Only digits, with a length of 10 digits (you can adjust this)
    val phoneNumberRegex = Regex("^[+]?[0-9]{10,13}\$")

    if (showDialog) {
        AlertDialog(
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White,
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { onDismiss() },

            title = { Text(stringResource(R.string.enter_your_phone_number)) },
            text = {
                Column {
                    TextField(
                        value = inputValue,
                        onValueChange = {
                            inputValue = it
                            errorMessage = "" // Reset error message on input change
                        },
                        label = { Text(stringResource(R.string.enter_a_valid_phone_number)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone
                        ),
                        isError = errorMessage.isNotEmpty()
                    )
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, PrimaryColor),
                    onClick = {
                        // Validation: Check if input matches the phone number regex
                        if (!phoneNumberRegex.matches(inputValue)) {
                            errorMessage =
                                context.getString(R.string.please_enter_a_valid_phone_number_10_digits)
                        } else {
                            onConfirm(inputValue)
                            onDismiss()
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        contentColor = Color.White
                    ),
                    onClick = { onDismiss() }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
