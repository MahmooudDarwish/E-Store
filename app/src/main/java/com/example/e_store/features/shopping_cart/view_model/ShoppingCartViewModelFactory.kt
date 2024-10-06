package com.example.e_store.features.shopping_cart.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository
import java.lang.IllegalArgumentException

class ShoppingCartViewModelFactory(private val _irepo: EStoreRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ShoppingCartViewModel::class.java)) {
            ShoppingCartViewModel(_irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}