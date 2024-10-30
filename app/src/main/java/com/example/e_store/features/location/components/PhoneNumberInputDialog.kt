package com.example.e_store.features.location.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_store.R
import com.example.e_store.ui.theme.PrimaryColor

@Composable
fun PhoneNumberInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    context: Context,
) {
    var inputValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Phone number regex: Only digits, with a length of 10 digits (you can adjust this)
    val phoneNumberRegex = Regex("^[+]?[0-9]{10,13}\$")

    if (showDialog) {
        AlertDialog(shape = RoundedCornerShape(16.dp),
            containerColor = Color.White,
            modifier = Modifier.padding(16.dp),
            onDismissRequest = { onDismiss() },

            title = { Text(stringResource(R.string.enter_your_phone_number)) },
            text = {
                Column {
                    TextField(value = inputValue,
                        onValueChange = {
                            inputValue = it
                            errorMessage = "" // Reset error message on input change
                        },
                        label = { Text(stringResource(R.string.enter_a_valid_phone_number)) },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone
                        ),
                        isError = errorMessage.isNotEmpty()
                    )
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, contentColor = Color.Black
                ), border = BorderStroke(1.dp, PrimaryColor), onClick = {
                    // Validation: Check if input matches the phone number regex
                    if (!phoneNumberRegex.matches(inputValue)) {
                        errorMessage =
                            context.getString(R.string.please_enter_a_valid_phone_number_10_digits)
                    } else {
                        onConfirm(inputValue)
                        onDismiss()
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor, contentColor = Color.White
                ), onClick = { onDismiss() }) {
                    Text(stringResource(R.string.cancel))
                }
            })
    }
}

