package com.example.e_store.features.location.components

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.e_store.utils.shared_components.BackButton
import com.example.e_store.utils.shared_components.ElevationCard
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

@Composable
fun MapScreenTopBar(navController: NavController, query: String, autocompleteLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val context = LocalContext.current
    Box(modifier = Modifier.padding(end = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BackButton {
                navController.popBackStack()
                navController.popBackStack()
            }
            ElevationCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                SearchMapBox(query = query, onClick = {
                    val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN,
                        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                    ).build(context)
                    autocompleteLauncher.launch(intent)
                })
            }
        }
    }
}