package com.example.e_store.features.location.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_store.features.location.view_model.AddLocationViewModel
import com.example.e_store.utils.shared_components.EShopButton
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.e_store.utils.constants.NavigationKeys
import com.example.e_store.utils.shared_models.Address

@Composable
fun AddLocationScreen(navController: NavController, viewModel: AddLocationViewModel = viewModel()) {
    val addressState = remember { mutableStateOf(Address()) }
    var isFormValid by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val address1 by viewModel.streetName.collectAsState()
    val city by viewModel.city.collectAsState()
    val phone by viewModel.phoneNumber.collectAsState()
    val firstName by viewModel.name.collectAsState()
    val save by viewModel.saveToAddress.collectAsState()



    Column(modifier = Modifier.padding(16.dp)) {

        // Name TextField
        OutlinedTextField(
            value = viewModel.name.collectAsState().value,
            onValueChange = {
                viewModel.updateName(it)
                validateForm(viewModel) { isValid, error ->
                    isFormValid = isValid
                    errorMessage = error
                }
            },
            label = { Text("Name*") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.contains("Name")
        )
        if (errorMessage.contains("Name")) {
            Text(text = errorMessage, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Phone Number TextField
        OutlinedTextField(
            value = viewModel.phoneNumber.collectAsState().value,
            onValueChange = {
                viewModel.updatePhoneNumber(it)
                validateForm(viewModel) { isValid, error ->
                    isFormValid = isValid
                    errorMessage = error
                }
            },
            label = { Text("Phone Number*") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.contains("Phone Number")
        )
        if (errorMessage.contains("Phone Number")) {
            Text(text = errorMessage, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Street Name TextField
        OutlinedTextField(
            value = viewModel.streetName.collectAsState().value,
            onValueChange = { viewModel.updateStreetName(it) },
            label = { Text("Street Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        // Home Number TextField
        OutlinedTextField(
            value = viewModel.city.collectAsState().value,
            onValueChange = { viewModel.updateCity(it) },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Save to Firebase Checkbox
        val saveToFirebase = viewModel.saveToAddress.collectAsState().value


        // Button to save the address
        EShopButton(
            onClick = {
                if (isFormValid) {
                    // Proceed to save address or navigate to map
                    viewModel.saveAddress(
                        Address(
                            address1 = viewModel.streetName.value,
                            city = viewModel.city.value,
                            phone = viewModel.phoneNumber.value,
                            firstName = viewModel.name.value,
                        )
                    )
                    navController.navigate(NavigationKeys.CHECKOUT_ROUTE)
                } else {
                    errorMessage = "Please fill all required fields correctly."
                }
            },
            text = "Use This Address",
            modifier = Modifier.fillMaxWidth()
        )

        EShopButton(
            onClick = {
                navController.navigate(NavigationKeys.MAP_ROUTE)
            },
            text = "Go To Map",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Validation logic
fun validateForm(viewModel: AddLocationViewModel, callback: (Boolean, String) -> Unit) {
    val name = viewModel.name.value
    val phone = viewModel.phoneNumber.value
    val city = viewModel.city.value

    when {
        name.isEmpty() -> callback(false, "Name is required.")
        phone.isEmpty() || !phone.matches(Regex("^[+]?[0-9]{10,13}\$")) -> callback(false, "Valid Phone Number is required.")
        city.isEmpty() -> callback(false, "City is required.")
        else -> callback(true, "")
    }
}


