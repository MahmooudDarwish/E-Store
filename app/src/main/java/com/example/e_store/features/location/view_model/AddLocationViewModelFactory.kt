package com.example.e_store.features.location.view_model

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository
import com.google.android.gms.location.FusedLocationProviderClient

class AddLocationViewModelFactory (
    val repository: EStoreRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddLocationViewModel::class.java)) {
            return AddLocationViewModel( repository ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}