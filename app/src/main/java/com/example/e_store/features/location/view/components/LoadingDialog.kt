package com.example.e_store.features.location.view.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.e_store.R
import com.example.e_store.utils.shared_components.LottieWithText

@Composable
fun LoadingDialog() {
    AlertDialog(
        onDismissRequest = { },
        title = {
            LottieWithText(
                lottieRawRes = R.raw.loading,
                displayText = stringResource(id = R.string.please_wait_till_we_process_your_request)
            )
        },
        confirmButton = { },
        dismissButton = null,
        containerColor = Color.White
    )
}
