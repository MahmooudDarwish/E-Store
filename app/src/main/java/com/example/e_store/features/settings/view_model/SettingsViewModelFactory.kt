package com.example.e_store.features.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.e_store.utils.data_layer.EStoreRepository

class SettingsViewModelFactory(private val _irepo: EStoreRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            SettingsViewModel(_irepo ) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}