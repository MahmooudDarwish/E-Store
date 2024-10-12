package com.example.e_store.features.location.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel ( val repository : EStoreRepository) : ViewModel() {


    private   val  _isAddressExist = MutableStateFlow(false)
    val isAddressExist = _isAddressExist.asStateFlow()

    private  val  _isPhoneExist = MutableStateFlow(false)
    val isPhoneExist = _isPhoneExist.asStateFlow()

    private   val _address = MutableStateFlow<List<Address?>>(emptyList())
    val address=_address.asStateFlow()


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

    fun fetchCustomerAddresses() {
        viewModelScope.launch {
            UserSession.shopifyCustomerID?.let {
                repository.fetchCustomerAddresses(it).collect { addresses ->
                    _address.value = addresses.addresses
                    Log.d("MapViewModel", "fetchCustomerAddresses: ${_address.value}")
                    Log.d("MapViewModel", "fetchCustomerAddresses: ${UserSession.shopifyCustomerID}")

                    UserSession.phone?.let { it1 -> isPhoneExistFUN(it1) }
                }
            }
        }
    }


    // Check if a phone number exists in the fetched addresses
    fun isPhoneExistFUN(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            Log.d("MapViewModel", "isPhoneExistFUN: $phoneNumber")

            // Assuming _address.value is a list of Address objects with a property 'phoneNumber'
            _isPhoneExist.value = _address.value.any { (it?.phone == phoneNumber) }
            Log.d("MapViewModel", "isPhoneExistFUN: ${_isPhoneExist.value}")

        } else {
            _isPhoneExist.value = false
        }

    }

}

