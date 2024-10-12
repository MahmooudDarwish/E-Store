package com.example.e_store.utils.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.e_store.R
import com.example.e_store.ui.theme.PrimaryColor

@Composable
fun sharedHeader(navController: NavController, headerText: String = "") {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 24.sp,
            color = PrimaryColor,
            fontStyle = FontStyle.Italic
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onBackClick = { navController.popBackStack() })

            Gap(width = 20)
            HeaderText(headerText = headerText)
        }
    }
}

@Preview
@Composable
fun sharedHeaderPreview() {
    sharedHeader( navController = NavHostController(LocalContext.current))
}
