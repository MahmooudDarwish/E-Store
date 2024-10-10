package com.example.e_store.features.payment.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository

class PaymentViewModelFactory (
    val repository: EStoreRepository

): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel( repository ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}