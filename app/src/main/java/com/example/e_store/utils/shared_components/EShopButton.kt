package com.example.e_store.utils.shared_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_store.R

@Composable
fun EShopButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = colorResource(id = R.color.colorPrimaryButton),
    textColor: Color = Color.White,
    fontSize: Float = 18f,
    cornerRadius: Int = 8
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        shape = RoundedCornerShape(cornerRadius.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize.sp
        )
    }
}
