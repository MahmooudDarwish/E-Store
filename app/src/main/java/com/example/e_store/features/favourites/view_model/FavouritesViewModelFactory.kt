package com.example.e_store.features.favourites.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository
import java.lang.IllegalArgumentException

class FavouritesViewModelFactory(private val repository: EStoreRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
            FavouritesViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}