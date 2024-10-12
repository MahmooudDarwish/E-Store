package com.example.e_store.features.location.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_store.features.location.view_model.AddLocationViewModel
import com.example.e_store.utils.shared_components.EShopButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.e_store.R
import com.example.e_store.utils.shared_components.sharedHeader
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.NavigationHolder
import kotlinx.coroutines.launch

@Composable
fun AddLocationScreen(navController: NavController, viewModel: AddLocationViewModel) {
    val isAddressExist by viewModel.isAddressExist.collectAsState()
    val isPhoneExist by viewModel.isPhoneExist.collectAsState()

    val addressState = remember { mutableStateOf(Address()) }
    var nameError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var cityError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }

    var isFormValid by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchCustomerAddresses()
    }

    // Handle navigation state based on NavigationHolder
    val isEditing = NavigationHolder.id != null
    DisposableEffect(Unit) {
        onDispose {
            NavigationHolder.id = null
        }
    }
    Column {
        // Change header text based on whether we are editing or adding
        val headerText = if (isEditing) {
            stringResource(id = R.string.edit_your_location)
        } else {
            stringResource(id = R.string.add_location)
        }

        sharedHeader(navController, headerText = headerText)
        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(16.dp)) {
            // Name TextField
            OutlinedTextField(
                value = viewModel.name.collectAsState().value,
                onValueChange = {
                    viewModel.updateName(it)
                    nameError =
                        if (it.isEmpty()) context.getString(R.string.name_is_required) else ""
                },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError.isNotEmpty()
            )
            if (nameError.isNotEmpty()) {
                Text(text = nameError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Phone Number TextField
            OutlinedTextField(
                value = viewModel.phoneNumber.collectAsState().value,
                onValueChange = {
                    viewModel.updatePhoneNumber(it)
                    if (it.matches(Regex("^[+]?[0-9]{10,13}\$"))) {
                        phoneError = ""
                        viewModel.isPhoneExistFUN(it)
                    } else {
                        phoneError = context.getString(R.string.valid_phone_number_is_required)
                    }
                },
                label = { Text(stringResource(R.string.phone_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                isError = phoneError.isNotEmpty() || isPhoneExist
            )
            if (isPhoneExist) {
                Text(text = stringResource(R.string.phone_already_exist), color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Street Name TextField
            OutlinedTextField(
                value = viewModel.streetName.collectAsState().value,
                onValueChange = {
                    viewModel.updateStreetName(it)
                    addressError =
                        if (it.isEmpty()) context.getString(R.string.address_is_required) else ""
                    viewModel.isAddressExistFUN(it)
                },
                label = { Text(stringResource(R.string.address)) },
                modifier = Modifier.fillMaxWidth(),
                isError = addressError.isNotEmpty() || isAddressExist
            )
            if (addressError.isNotEmpty()) {
                Text(text = addressError, color = Color.Red)
            }
            if (isAddressExist) {
                Text(text = stringResource(id = R.string.address_already_exist), color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // City TextField
            OutlinedTextField(
                value = viewModel.city.collectAsState().value,
                onValueChange = {
                    viewModel.updateCity(it)
                    cityError =
                        if (it.isEmpty()) context.getString(R.string.city_is_required) else ""
                },
                label = { Text(stringResource(R.string.city)) },
                modifier = Modifier.fillMaxWidth(),
                isError = cityError.isNotEmpty()
            )
            if (cityError.isNotEmpty()) {
                Text(text = cityError, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Button to save the address
            EShopButton(
                onClick = {
                    coroutineScope.launch {
                        validateForm(context, viewModel) { isValid, errors ->
                            isFormValid = isValid
                            nameError = errors["name"].orEmpty()
                            phoneError = errors["phone"].orEmpty()
                            addressError = errors["address"].orEmpty()
                            cityError = errors["city"].orEmpty()

                            if (isFormValid && !isAddressExist && !isPhoneExist) {
                               if (isEditing){
                                   viewModel.updateDefaultLocation(
                                       Address(
                                           address1 = viewModel.streetName.value,
                                           city = viewModel.city.value,
                                           phone = viewModel.phoneNumber.value,
                                           firstName = viewModel.name.value,
                                       )
                                   )

                               }else{
                                viewModel.saveAddress(
                                    Address(
                                        address1 = viewModel.streetName.value,
                                        city = viewModel.city.value,
                                        phone = viewModel.phoneNumber.value,
                                        firstName = viewModel.name.value,
                                    )
                                )
                               }
                                navController.navigate(NavigationKeys.LOCATION_ROUTE)
                            }
                        }
                    }
                },
                text = if (isEditing) {
                    stringResource(id = R.string.edit_your_location)
                } else {
                    stringResource(id = R.string.use_this_address)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Hide the "Go to Map" button if in edit mode
            if (!isEditing) {
                EShopButton(
                    onClick = {
                        navController.navigate(NavigationKeys.MAP_ROUTE)
                    },
                    text = stringResource(R.string.go_to_map),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

    // Validation logic with field-specific errors
    fun validateForm(
        context: Context,
        viewModel: AddLocationViewModel,
        callback: (Boolean, Map<String, String>) -> Unit
    ) {
        val name = viewModel.name.value
        val phone = viewModel.phoneNumber.value
        val city = viewModel.city.value

        val errors = mutableMapOf<String, String>()

        if (name.isEmpty()) {
            errors["name"] = context.getString(R.string.name_is_required)
        }
        if (phone.isEmpty() || !phone.matches(Regex("^[+]?[0-9]{10,13}\$"))) {
            errors["phone"] = context.getString(R.string.valid_phone_number_is_required)
        }
        if (city.isEmpty()) {
            errors["city"] = context.getString(R.string.city_is_required)
        }
        if (viewModel.streetName.value.isEmpty()) {
            errors["address"] = context.getString(R.string.address_is_required)
        }

        // If no errors, form is valid
        val isValid = errors.isEmpty()
        callback(isValid, errors)
    }

