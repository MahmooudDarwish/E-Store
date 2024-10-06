package com.example.e_store.features.product_info.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductInfoViewModelFactory (): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductInfoViewModel::class.java)) {
            return ProductInfoViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}