package com.example.e_store.features.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_models.ProductDetails.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {

    val currencyOptions = listOf("Select Currency Type","Egyptian Pound", "Dollar", "Euro", "Saudi Riyal", "United Arab Emirates Dirham")
    var selectedCurrency by remember { mutableStateOf(currencyOptions[0]) }
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)

    ){
        ElevationCard {
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },

                ) {
                TextField(
                    value = selectedCurrency,
                    onValueChange = {},
                    readOnly = true, // Make the TextField read-only
                    label = { Text("Your Currency") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clickable { expanded = !expanded } // Trigger menu on click
                )
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(Color.White),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currencyOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedCurrency = selectionOption
                                expanded = false // Close the dropdown when an option is selected
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
