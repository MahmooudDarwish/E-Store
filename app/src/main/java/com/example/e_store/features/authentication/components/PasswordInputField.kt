package com.example.e_store.features.authentication.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.e_store.R

@Composable
fun PasswordInputField(
    value: String, onValueChange: (String) -> Unit, modifier: Modifier, label: Int
) {
    val passwordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = stringResource(id =label)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image =
                if (passwordVisible.value) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(
                    painter = painterResource(id = image),
                    contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                )
            }
        },
        modifier = modifier
    )
}