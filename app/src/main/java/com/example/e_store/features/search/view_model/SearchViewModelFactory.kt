package com.example.e_store.features.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository

class SearchViewModelFactory (private val _irepo: EStoreRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            SearchViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")

        }
    }
}