package com.example.e_store.features.authentication.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.e_store.R


@Composable
fun PhoneNumberInputField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = stringResource(id = R.string.phone_label)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone
        ),
        modifier = modifier
    )
}
