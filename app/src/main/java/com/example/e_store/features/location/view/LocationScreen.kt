package com.example.e_store.features.location.view


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.e_store.R
import com.example.e_store.features.location.components.LoadingDialog
import com.example.e_store.features.location.view_model.LocationViewModel
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.navigation.Screen
import com.example.e_store.utils.shared_components.ConfirmNegativeActionDialog
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_components.LottieWithText
import com.example.e_store.utils.shared_components.sharedHeader
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DeletionState
import com.example.e_store.utils.shared_models.NavigationHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(navController: NavController, viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    val addressDetails = remember { mutableStateOf<AddressResponse?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var deletingLocation by remember { mutableStateOf(false) }
    var deletionId by remember {
        mutableStateOf<Long?>(null)
    }
    var locationToDelete by remember { mutableStateOf<Address?>(null) }
    val deletionState by viewModel.deletionState.collectAsStateWithLifecycle()

    when (deletionState) {
        is DeletionState.CannotDelete -> {
            val message = (deletionState as DeletionState.CannotDelete).message
            com.example.e_store.utils.shared_components.Popup(
                showDialog = true,
                onDismiss = {
                    viewModel.deletionState.value = DeletionState.CanDelete // Reset state
                },
                title = stringResource(R.string.warning),
                body = message,
                confirmButtonText = stringResource(R.string.edit),
                onConfirm = {
                    // Navigate to edit address
                    NavigationHolder.id = deletionId

                    NavigationHolder.address1 = locationToDelete!!.address1
                    NavigationHolder.city = locationToDelete!!.city
                    NavigationHolder.phone = locationToDelete!!.phone
                    NavigationHolder.firstName = locationToDelete!!.first_name
                    NavigationHolder.country = locationToDelete!!.country
                    NavigationHolder.default = locationToDelete!!.default!!

                    NavigationHolder.country_code = locationToDelete!!.country_code
                    Log.d("country_code", "dddd : ${locationToDelete!!.country_code}")


                    navController.navigate(NavigationKeys.ADD_LOCATION_ROUTE)
                    viewModel.deletionState.value = DeletionState.CanDelete // Reset state
                },
                dismissButtonText = stringResource(id = R.string.cancel)
            )
        }

        is DeletionState.CanDelete -> {

        }
    }


    val coroutineScope = rememberCoroutineScope()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    Log.d("LocationScreen", "currentBackStackEntry: ${Screen.Location.route}")
    Log.d(
        "LocationScreen",
        "currentBackStackEntry.value: ${currentBackStackEntry.value?.destination?.route}"
    )

    LaunchedEffect(currentBackStackEntry.value) {
        if (currentBackStackEntry.value?.destination?.route == Screen.Location.route) {
            Log.d("LocationScreen", "Fetching locations")
            // Re-fetch locations when returning to this scree
            viewModel.fetchAllLocations()
        }
    }

    if (deletingLocation) {
        LoadingDialog()
    }
    LaunchedEffect(deletingLocation) {
        delay(2000)
        deletingLocation = false

    }
    if (showDialog && locationToDelete != null) {
        ConfirmNegativeActionDialog(showDialog = showDialog,
            title = (stringResource(R.string.remove_from_your_location)),
            message = (stringResource(R.string.are_you_sure_you_want_to_remove_this_location_from_your_list)),
            onConfirm = {
                coroutineScope.launch {
                    deletionId = locationToDelete!!.id!!
                    viewModel.deleteLocation(locationToDelete!!.id!!, locationToDelete!!.default!!)
                    deletingLocation = true
                    showDialog = false

                }
                showDialog = false
            }) {
            showDialog = false
        }

    }



    when (locations) {
        is DataState.Loading -> {
            isLoading.value = true
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val addressResponse = (locations as DataState.Success<AddressResponse?>).data
            isLoading.value = false

            addressResponse?.let {
                Log.d("AddressResponse", it.toString())
                addressDetails.value = it
            }
        }

        is DataState.Error -> {
            val errorMsg = (locations as DataState.Error).message
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
            isLoading.value = false
        }
    }
    Log.d("LocationScreen", "isLoading.value: ${isLoading.value}")



    Scaffold(
        containerColor = Color.White,
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(NavigationKeys.ADD_LOCATION_ROUTE) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Location")
                }
            }
        }
    ) { padding ->
        Column {
            sharedHeader(
                headerText = stringResource(R.string.location),
                navController = navController
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (!isLoading.value) {
                addressDetails.value?.let { details ->
                    if (details.addresses.isNotEmpty()) {
                        LazyColumn(
                            contentPadding = padding,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 60.dp)
                        ) {

                            items(details.addresses) { location ->
                                Log.d("LocationScreen", "Location: $location")

                                LocationItem(
                                    location = location,
                                    onDelete = {
                                        locationToDelete = location
                                        showDialog = true
                                    },
                                    onEdit = {

                                        NavigationHolder.id = location.id!!

                                        NavigationHolder.default = location.default!!
                                        NavigationHolder.address1 = location.address1
                                        NavigationHolder.city = location.city
                                        NavigationHolder.phone = location.phone
                                        NavigationHolder.firstName = location.first_name
                                        NavigationHolder.country = location.country
                                        NavigationHolder.country_code = location.country_code

                                        navController.navigate(NavigationKeys.ADD_LOCATION_ROUTE)
                                    },
                                    onMakeDefault = {
                                        viewModel.makeDefaultLocation(location.id!!, location)
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.default_location_updated),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    } else {
                        LottieWithText(
                            displayText = stringResource(R.string.no_locations_found),
                            lottieRawRes = R.raw.no_data_found)
                    }
                } ?: LottieWithText(
                    displayText = stringResource(R.string.no_locations_found),
                    lottieRawRes = R.raw.no_data_found

                )
            } else {
                EShopLoadingIndicator()
            }

        }
    }

}

@Composable
fun LocationItem(
    location: Address,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onMakeDefault: () -> Unit,
) {
    ElevationCard(
        modifier = Modifier
            .clickable(onClick = { onMakeDefault() })
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {

                if (location.default == true) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            painter = painterResource(
                                id = R.drawable.icon_default_location
                            ),
                            contentDescription = stringResource(R.string.default_location),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.default_location),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp
                        )
                    }

                }


                // City description and value
                Text(
                    text = stringResource(id = R.string.city),
                    style = MaterialTheme.typography.bodySmall, // Label style
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = location.city ?: stringResource(R.string.unknown_city),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Address description and value
                Text(
                    text = stringResource(id = R.string.address),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = location.address1 ?: "No address provided",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Phone description and value (if available)
                location.phone?.let {
                    Text(
                        text = stringResource(R.string.phone),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column {
                // Delete button icon
                IconButton(
                    onClick = { onDelete() },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = CircleShape
                        )
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_location),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                IconButton(
                    onClick = { onEdit() },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = CircleShape
                        )
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_location),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

            }

        }
    }
}
