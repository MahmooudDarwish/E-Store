package com.example.e_store.features.location.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.e_store.features.location.view_model.LocationViewModel
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import com.example.e_store.utils.shared_models.UserSession

@Composable
fun LocationScreen(navController: NavController, viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locations by viewModel.locations.collectAsStateWithLifecycle()
    var addressDetails = remember { mutableStateOf<AddressResponse?>(null) }
    var isLoading = remember { mutableStateOf(true) } // Keep track of loading state

    // Fetch locations when the screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchAllLocations()
    }

    // Handle different states of locations
    when (locations) {
        is DataState.Loading -> {
            isLoading.value = true
            EShopLoadingIndicator() // Show loading indicator
        }

        is DataState.Success -> {
            val addressResponse = (locations as DataState.Success<AddressResponse?>).data
            addressResponse?.let {
                addressDetails.value = it
                isLoading.value = false // Data loaded successfully, stop loading
            }
        }

        is DataState.Error -> {
            val errorMsg = (locations as DataState.Error).message
            LaunchedEffect(Unit) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
            isLoading.value = false // Stop loading when there's an error
        }

        else -> {
            // Handle any unknown states or do nothing
        }
    }

    if (!isLoading.value) { // Only display content when not loading
        addressDetails.value?.let { details ->

            if (details.addresses.isEmpty()) {
                // Navigate to add a location if no addresses are found
                navController.navigate(NavigationKeys.ADD_LOCATION_ROUTE)
            } else {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(NavigationKeys.ADD_LOCATION_ROUTE)
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Location")
                        }
                    }
                ) { padding ->
                    // Display the list of saved locations using LazyColumn
                    LazyColumn(
                        contentPadding = padding,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(details.addresses) { location ->
                            LocationItem(location = location, onDelete = {
                                location.id?.let {
                                    Log.d("LocationScreen", "Deleting location with ID: $it")
                                    location.default?.let { it1 ->
                                        viewModel.deleteLocation(it,
                                            it1
                                        )
                                    }
                                }
                            }, onMakeDefault = { viewModel.makeDefaultLocation(location.id!!,location)
                            navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LocationItem(location: Address, onDelete: () -> Unit, onMakeDefault: () -> Unit) {
    ElevationCard(
        modifier = Modifier
            .clickable( onClick = {onMakeDefault()})

            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Align items at both ends
        )
        {
            Column(

            ) {
                location.address1?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1

                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                location.city?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1

                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete, // Use an appropriate delete icon
                    contentDescription = "Delete Location"
                )
            }
        }
    }

}



