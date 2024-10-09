package com.example.e_store.features.shopping_cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.e_store.R
import com.example.e_store.features.shopping_cart.view_model.ShoppingCartViewModel
import com.example.e_store.utils.shared_components.ConfirmNegativeActionDialog
import com.example.e_store.utils.shared_components.ElevationCard
import com.example.e_store.utils.shared_models.LineItem
import kotlinx.coroutines.launch

@Composable
fun ShoppingCartItem(
    item: LineItem,
    viewModel: ShoppingCartViewModel,
    onItemDeleted: (LineItem) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    ConfirmNegativeActionDialog(showDialog = showDialog,
        title = stringResource(R.string.remove_from_shopping_cart),
        message = stringResource(R.string.are_you_sure_you_want_to_remove_this_item_from_your_shopping_cart),
        onConfirm = {
            coroutineScope.launch { onItemDeleted(item) }
            showDialog = false
        }) {
        showDialog = false
    }

    ElevationCard() {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(imageUrl = item.properties.getOrNull(2)?.value ?: "")

            Spacer(modifier = Modifier.width(8.dp))

            ProductInfo(
                item = item,
                viewModel = viewModel,
                onItemDeleted = onItemDeleted,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = CircleShape
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Favourite",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
