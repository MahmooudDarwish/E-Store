package com.example.e_store.features.product_info.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository

class ProductInfoViewModelFactory (private val _irepo: EStoreRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductInfoViewModel::class.java)) {
            return ProductInfoViewModel(_irepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}