package com.example.e_store.features.location.view

import android.location.Geocoder
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.e_store.R
import com.example.e_store.features.location.view.components.LoadingDialog
import com.example.e_store.features.location.view_model.LocationViewModel
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.ConfirmNegativeActionDialog
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_components.LottieWithText
import com.example.e_store.utils.shared_components.sharedHeader
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DeletionState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import com.example.e_store.utils.shared_models.NavigationHolder
import com.example.e_store.utils.shared_models.UserAddress
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun LocationScreen(navController: NavController, viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    var addressDetails = remember { mutableStateOf<AddressResponse?>(null) }
    var isLoading = remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var deletingLocation by remember { mutableStateOf(false) }

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
                    navController.navigate(NavigationKeys.ADD_LOCATION_ROUTE)
                    viewModel.deletionState.value = DeletionState.CanDelete // Reset state
                },
                dismissButtonText = stringResource(id = R.string.cancel)
            )
        }
        is DeletionState.CanDelete -> {

        }
    }

    var locationToDelete by remember { mutableStateOf<Address?>(null) }

    val coroutineScope = rememberCoroutineScope()

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
                    viewModel.deleteLocation(locationToDelete!!.id!!, locationToDelete!!.default!!)
                    deletingLocation = true
                    showDialog = false

                }
                showDialog = false
            }) {
            showDialog = false
        }

    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllLocations()
    }

    when (locations) {
        is DataState.Loading -> {
            isLoading.value = true
            EShopLoadingIndicator()
        }

        is DataState.Success -> {
            val addressResponse = (locations as DataState.Success<AddressResponse?>).data
            addressResponse?.let {
                addressDetails.value = it
                isLoading.value = false
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

    if (!isLoading.value) {
        addressDetails.value?.let { details ->
            if (details.addresses.isNotEmpty()) {
                Column {
                    sharedHeader(navController, headerText = stringResource(id = R.string.location))
                    Spacer(modifier = Modifier.height(16.dp))

                    Scaffold(
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
                        LazyColumn(
                            contentPadding = padding,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(details.addresses) { location ->
                                LocationItem(
                                    location = location,
                                    onDelete = {
                                        locationToDelete = location
                                        showDialog = true
                                    },
                                    onMakeDefault = {
                                        viewModel.makeDefaultLocation(location.id!!, location)
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }else{
                LottieWithText(
                    displayText = stringResource(R.string.no_locations_found),
                    lottieRawRes = R.raw.no_data_found
                )
            }
        }
    }
}

@Composable
fun LocationItem(location: Address, onDelete: () -> Unit, onMakeDefault: () -> Unit) {
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
                // City description and value
                Text(
                    text = "City:",
                    style = MaterialTheme.typography.bodySmall, // Label style
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = location.city ?: "Unknown city",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Address description and value
                Text(
                    text = "Address:",
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
                        text = "Phone:",
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
                    contentDescription = "Delete Location",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
