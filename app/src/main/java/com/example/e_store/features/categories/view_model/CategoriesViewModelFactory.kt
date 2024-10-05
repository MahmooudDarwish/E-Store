package com.example.e_store.features.categories.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository
import java.lang.IllegalArgumentException

class CategoriesViewModelFactory(private val _irepo: EStoreRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            CategoriesViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}