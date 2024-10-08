package com.example.e_store.features.shopping_cart.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_store.R
import com.example.e_store.ui.theme.PrimaryColor
import com.example.e_store.utils.shared_components.Gap

@Composable
fun QuantityControl(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDecrease) {
            Icon(
                painter = painterResource(R.drawable.ic_minus_square),
                contentDescription = stringResource(R.string.decrease_quantity),
                modifier = Modifier.size(30.dp)
            )
        }


        Gap(width = 8)

        Text(text = quantity.toString(), fontSize = 20.sp)

        Gap(width = 8)

        IconButton(onClick = onIncrease) {
            Icon(
                painter = painterResource(R.drawable.ic_add_square),
                contentDescription = stringResource(R.string.increase_quantity),
                modifier = Modifier.size(30.dp),
                tint = PrimaryColor
            )
        }
    }
}
