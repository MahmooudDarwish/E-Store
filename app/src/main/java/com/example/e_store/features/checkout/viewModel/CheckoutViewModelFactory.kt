package com.example.e_store.features.checkout.viewModel

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository

class CheckoutViewModelFactory (
    private val repo: EStoreRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(  repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}