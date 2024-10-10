package com.example.e_store.features.location.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.launch

class MapViewModel ( val repository : EStoreRepository) : ViewModel() {


    fun saveAddress(address: Address) {

        val addressToSave = AddNewAddress(
            address = address
        )

        viewModelScope.launch {
            UserSession.shopifyCustomerID?.let { repository.createCustomerAddress(it,addressToSave) }
        }

        Log.d("MapViewModel", "Address saved: $address")
        Log.d("MapViewModel", "Customer ID: ${UserSession.shopifyCustomerID}")
    }

}

