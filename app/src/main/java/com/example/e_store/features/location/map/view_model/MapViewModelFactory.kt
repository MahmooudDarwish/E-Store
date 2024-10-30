package com.example.e_store.features.location.map.view_model

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository

class MapViewModelFactory (
    val repository: EStoreRepository,
    val geocoder: Geocoder

): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel( repository, geocoder ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}