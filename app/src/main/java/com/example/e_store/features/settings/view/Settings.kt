package com.example.e_store.features.settings.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.e_store.R
import com.example.e_store.features.settings.view_model.SettingsViewModel
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.EShopLoadingIndicator
import com.example.e_store.utils.shared_components.Gap
import com.example.e_store.utils.shared_components.HeaderText
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    val currencyResponse by viewModel.currencyResponse.collectAsState()

    when (currencyResponse) {
        is DataState.Success -> {
            val response = (currencyResponse as DataState.Success).data
            SettingsComponents(
                navController,
                FirebaseAuth.getInstance().currentUser!!.uid,
                LocalContext.current
            )
        }

        is DataState.Error -> {
            val error = (currencyResponse as DataState.Error).message
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        }

        is DataState.Loading -> {
            EShopLoadingIndicator()
        }
    }

    Log.d("SettingsScreenResponse", "SettingsScreen: $currencyResponse")

    LaunchedEffect(Unit) {
        viewModel.fetchCurrency()
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsComponents(
    navController: NavController,
    userId: String, // Pass the userId here
    context: Context // Needed for the update method
) {

    val currencyOptions = listOf(
        "Select Currency Type",
        "US Dollar",
        "Australian Dollar",
        "Bulgarian Lev",
        "Canadian Dollar",
        "Swiss Franc",
        "Chinese Yuan",
        "Egyptian Pound",
        "Euro",
        "British Pound"
    )

    val currencyCodeMap = mapOf(
        "US Dollar" to "USD",
        "Australian Dollar" to "AUD",
        "Bulgarian Lev" to "BGN",
        "Canadian Dollar" to "CAD",
        "Swiss Franc" to "CHF",
        "Chinese Yuan" to "CNY",
        "Egyptian Pound" to "EGP",
        "Euro" to "EUR",
        "British Pound" to "GBP"
    )

    val currencyName = currencyCodeMap.entries.firstOrNull { it.value == UserSession.currency }?.key

    var selectedCurrency by remember { mutableStateOf(currencyName) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = stringResource(R.string.app_name),
            fontSize = 24.sp,
            color = PrimaryColor,
            fontStyle = FontStyle.Italic
        )

        SettingsHeader(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {

            Text(
                text = "Currency Converter",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select Your Currency",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    ) {
                        selectedCurrency?.let {
                            TextField(
                                value = it,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Your Currency") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedLabelColor = PrimaryColor,
                                    unfocusedLabelColor = PrimaryColor,
                                    focusedIndicatorColor = PrimaryColor,
                                    unfocusedIndicatorColor = PrimaryColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .clickable { expanded = !expanded }
                            )
                        }
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            currencyOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        selectedCurrency = selectionOption
                                        expanded = false

                                        if (selectedCurrency != "Select Currency Type") {
                                            val selectedCurrencyCode =
                                                currencyCodeMap[selectedCurrency]
                                            if (selectedCurrencyCode != null) {
                                                updateUserCurrency(
                                                    context,
                                                    userId,
                                                    selectedCurrencyCode
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About Us",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Team Members",
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "\tKareem Ashraf",
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "\tMahmoud Darwish",
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "\tMohammed Abdelraheem",
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}


@Composable
fun SettingsHeader(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBackClick = { navController.popBackStack() })

        Gap(width = 20)
        HeaderText(headerText = stringResource(id = R.string.settings))
    }
}


private fun updateUserCurrency(context: Context, userId: String, newCurrency: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users").document(userId).update("currency", newCurrency)
        .addOnSuccessListener {
            Toast.makeText(context, "Currency updated successfully", Toast.LENGTH_SHORT).show()
            Log.d("Firestore", "User currency updated to $newCurrency")
            UserSession.currency = newCurrency
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error updating currency: ${e.message}", Toast.LENGTH_SHORT)
                .show()
            Log.e("Firestore", "Error updating currency", e)
        }
}




