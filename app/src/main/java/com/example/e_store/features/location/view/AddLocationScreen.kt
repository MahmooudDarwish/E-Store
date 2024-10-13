package com.example.e_store.features.location.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.e_store.features.location.view_model.AddLocationViewModel
import com.example.e_store.utils.shared_components.EShopButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.e_store.R
import com.example.e_store.utils.shared_components.sharedHeader
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_components.LottieWithText
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.CountryInfo
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.GeoNameLocation
import com.example.e_store.utils.shared_models.NavigationHolder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddLocationScreen(navController: NavController, viewModel: AddLocationViewModel) {
    val countriesState by viewModel.countries.collectAsState()
    val citiesState by viewModel.cities.collectAsState()
    val selectedCountry by viewModel.selectedCountry.collectAsState()

    val isAddressExist by viewModel.isAddressExist.collectAsState()
    val isPhoneExist by viewModel.isPhoneExist.collectAsState()

    var nameError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var countryError by remember { mutableStateOf("") }
    var cityError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var showCityDialog by remember { mutableStateOf(false) }

    var selectedCountryName by remember { mutableStateOf("") }
    var searchCountryText by remember { mutableStateOf("") }
    var filteredCountryList by remember { mutableStateOf(emptyList<CountryInfo>()) }
    var selectedCityName by remember { mutableStateOf("") }
    var searchCityText by remember { mutableStateOf("") }
    var filteredCityList by remember { mutableStateOf(emptyList<GeoNameLocation>()) }

    var showCountryDialog by remember { mutableStateOf(false) }


    var isFormValid by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getCountries()
        viewModel.fetchCustomerAddresses()


    }
    LaunchedEffect(countriesState) {
        Log.d("TAG", "AddLocationScreen: $countriesState")
        if (countriesState is DataState.Success) {
            val countryList = (countriesState as DataState.Success<List<CountryInfo>>).data
            filteredCountryList = countryList
        }
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

            OutlinedTextField(
                enabled = false,
                value = viewModel.selectedCountry.collectAsState().value?.countryName ?: "Country",
                onValueChange = {

                    if (viewModel.selectedCountry.value!!.countryName.isEmpty()) {
                        countryError =
                            if (it.isEmpty()) context.getString(R.string.country_is_required) else ""
                    }
                },
                label = { Text(stringResource(R.string.country)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showCountryDialog = true
                    },
                isError = countryError.isNotEmpty(),
            )
            if (countryError.isNotEmpty()) {
                Text(text = countryError, color = Color.Red)
            }


            OutlinedTextField(
                enabled = false,
                value = viewModel.selectedCity.collectAsState().value?.name ?: "City",
                onValueChange = {
                    Log.d("TAG", "AddLocationScreen: updateCity$it")
                    if (viewModel.selectedCity.value!!.name.isEmpty()) {
                        cityError =
                            if (it.isEmpty()) context.getString(R.string.city_is_required) else ""
                    }
                },
                label = { Text(stringResource(R.string.city)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Log.d("TAG", "AddLocationScreen: ")
                        if (selectedCountry != null) {
                            showCityDialog = true

                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Please select a country first",
                                    Toast.LENGTH_SHORT
                                )
                                .show()

                        }
                    },
                isError = cityError.isNotEmpty(),

                )
            if (cityError.isNotEmpty()) {
                Text(text = cityError, color = Color.Red)
            }

            // City TextField

            if (showCountryDialog) {
                AlertDialog(
                    backgroundColor = Color.White,
                    onDismissRequest = { showCountryDialog = false },
                    title = { Text(stringResource(R.string.select_country)) },
                    text = {
                        Column {
                            // Search Field
                            OutlinedTextField(
                                value = searchCountryText,
                                onValueChange = {
                                    searchCountryText = it
                                },
                                label = { Text(stringResource(R.string.search_country)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Update filtered list based on search input
                            LaunchedEffect(searchCountryText, countriesState) {
                                if (countriesState is DataState.Success) {
                                    val countryList =
                                        (countriesState as DataState.Success<List<CountryInfo>>).data
                                    filteredCountryList = if (searchCountryText.isEmpty()) {
                                        countryList
                                    } else {
                                        countryList.filter {
                                            it.countryName.contains(
                                                searchCountryText,
                                                ignoreCase = true
                                            )
                                        }
                                    }
                                }
                            }

                            // LazyColumn to display filtered countries
                            LazyColumn {
                                items(filteredCountryList) { country ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedCountryName = country.countryName
                                            viewModel.selectCountry(country)
                                            viewModel.updateCountry(selectedCountryName)
                                            viewModel.selectCity(city = null)
                                            viewModel.updateCity(null)
                                            showCountryDialog = false
                                            searchCountryText = "" // Reset search text on selection
                                        }
                                    ) {
                                        Text(country.countryName)
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showCountryDialog = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // City Dropdown with Search (Disabled until a country is selected)


            if (showCityDialog) {
                AlertDialog(
                    backgroundColor = Color.White,
                    onDismissRequest = { showCityDialog = false },
                    title = { Text(stringResource(R.string.select_city)) },
                    text = {
                        Column {
                            // Search Field
                            OutlinedTextField(
                                value = searchCityText,
                                onValueChange = { searchCityText = it },
                                label = { Text(stringResource(R.string.search_city)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // LazyColumn to display filtered cities
                            if (citiesState is DataState.Success) {
                                val cityList =
                                    (citiesState as DataState.Success<List<GeoNameLocation>>).data

                                // Update the filtered list based on search input
                                filteredCityList = if (searchCityText.isEmpty()) {
                                    cityList
                                } else {
                                    cityList.filter {
                                        it.name.contains(
                                            searchCityText,
                                            ignoreCase = true
                                        )
                                    }
                                }

                                LazyColumn {
                                    items(filteredCityList) { city ->
                                        DropdownMenuItem(
                                            onClick = {
                                                selectedCityName = city.name
                                                viewModel.selectCity(city = city)
                                                viewModel.updateCity(selectedCountryName)
                                                showCityDialog = false
                                                searchCityText =
                                                    ""
                                            }
                                        ) {
                                            Text(city.name)
                                        }
                                    }
                                }
                            } else {
                                LottieWithText(
                                    lottieRawRes = R.raw.no_data_found,
                                    displayText = "No Cities Found"
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showCityDialog = false }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                )
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
                            countryError = errors["country"].orEmpty()

                            if (isFormValid && !isAddressExist && !isPhoneExist) {
                                if (isEditing) {
                                    viewModel.updateDefaultLocation(
                                        Address(
                                            address1 = viewModel.streetName.value,
                                            city = viewModel.city.value,
                                            phone = viewModel.phoneNumber.value,
                                            firstName = viewModel.name.value,
                                        )
                                    )

                                } else {
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
    callback: (Boolean, Map<String, String>) -> Unit,
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
    if (city.isNullOrEmpty()) {
        errors["city"] = context.getString(R.string.city_is_required)
    }
    if (viewModel.streetName.value.isEmpty()) {
        errors["address"] = context.getString(R.string.address_is_required)
    }
    if (viewModel.country.value.isEmpty()) {
        errors["country"] = context.getString(R.string.country_is_required)
    }

    // If no errors, form is valid
    val isValid = errors.isEmpty()
    callback(isValid, errors)
}
