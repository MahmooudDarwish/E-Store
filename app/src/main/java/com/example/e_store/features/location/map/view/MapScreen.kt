package com.example.e_store.features.location.map.view


import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.e_store.R
import com.example.e_store.features.location.map.view_model.MapViewModel
import com.example.e_store.utils.shared_models.UserSession
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.example.e_store.BuildConfig
import com.example.e_store.features.location.components.GpsPreemptionScreen
import com.example.e_store.features.location.components.MapScreenFABs
import com.example.e_store.features.location.components.MapScreenTopBar
import com.example.e_store.features.location.components.PhoneNumberInputDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MapScreen(navController: NavController, viewModel: MapViewModel) {
    val context = LocalContext.current
    val isPhoneExist by viewModel.isPhoneExist.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showPhoneNumberDialog by rememberSaveable { mutableStateOf(false) }
    var isMapLoaded by rememberSaveable { mutableStateOf(false) }
    var inputValue by rememberSaveable { mutableStateOf(UserSession.phone) }
    var query by rememberSaveable { mutableStateOf(context.getString(R.string.search)) }
    var currentLocation by remember { mutableStateOf(LatLng(38.7946, 106.5348)) }
    val markerState = rememberMarkerState(position = currentLocation)
    var requestPermission by remember { mutableStateOf(false) }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            currentLocation, 5f, 0f, 0f
        )
    }

    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    LaunchedEffect(Unit) {
        viewModel.fetchCustomerAddresses()
    }

    LaunchedEffect(requestPermission) {
        delay(4000)
        requestPermission = false
    }

    if (requestPermission) {
        GpsPreemptionScreen(onGpsAndPermissionStatusChange = { isGpsEnabled, isPermissionGranted ->
            if (isGpsEnabled && isPermissionGranted) {

                viewModel.fetchLocation(fusedLocationClient) { location ->
                    location?.let {
                        val currentLocation = LatLng(it.latitude, it.longitude)
                        cameraPositionState.position = CameraPosition(currentLocation, 5f, 0f, 0f)
                        markerState.position = currentLocation
                        // Show a success snackbar message
                    } ?: run {
                        Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }



    LaunchedEffect(isMapLoaded) {
        Places.initialize(context, BuildConfig.GOOGLE_MAPS_API_KEY)

        isPhoneExist.let {
            if (it) {
                showPhoneNumberDialog = true
            }
        }

        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


    PhoneNumberInputDialog(
        showDialog = showPhoneNumberDialog,
        onDismiss = { showPhoneNumberDialog = false },
        onConfirm = { phoneNumber ->
            inputValue = phoneNumber
            showPhoneNumberDialog = false
            viewModel.getAddressDetails(currentLocation, inputValue)
            Toast.makeText(context, "Location Saved", Toast.LENGTH_SHORT).show()

        },
        context
    )
    inputValue?.let { viewModel.isPhoneExistFUN(it) }


    val autocompleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val place = Autocomplete.getPlaceFromIntent(data)

                val latLng = place.location

                latLng?.let {
                    currentLocation = LatLng(latLng.latitude, latLng.longitude)
                    cameraPositionState.position = CameraPosition(
                        currentLocation, // Move to the new position
                        5f, // Zoom level
                        0f, 0f
                    )
                    markerState.position = currentLocation

                    query = place.displayName ?: context.getString(R.string.search)
                }
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            val status: Status? = result.data?.let { Autocomplete.getStatusFromIntent(it) }
            Log.e("Autocomplete", "Autocomplete canceled: $status")
        }
    }




    Scaffold(containerColor = Color.White,

        topBar = {
            MapScreenTopBar(navController, query, autocompleteLauncher)
        }, floatingActionButton = {


            MapScreenFABs(
                viewModel,
                fusedLocationClient,
                cameraPositionState,
                markerState,
                phoneNumber = inputValue.toString(),
                onClick = {
                    requestPermission = true
                }

            )


        }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
        ) {

            GoogleMap(modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                ),
                onMapLoaded = {
                },
                onMapClick = { latLng ->
                    // Update currentLocation
                    currentLocation = LatLng(latLng.latitude, latLng.longitude)
                    markerState.position = currentLocation

                    cameraPositionState.position = CameraPosition(
                        currentLocation, // Move to the new position
                        5f, // Zoom level
                        0f, 0f
                    )
                    query = context.getString(R.string.search)
                }) {
                Marker(
                    state = markerState,
                    title = stringResource(R.string.your_location),
                    snippet = stringResource(R.string.current_location)
                )
            }


        }
    }

}

