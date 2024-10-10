package com.example.e_store.features.location.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddLocationViewModel (val repository : EStoreRepository): ViewModel() {

    // MutableStateFlow for each field
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name


    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _streetName = MutableStateFlow("")
    val streetName: StateFlow<String> = _streetName

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _saveToAddress = MutableStateFlow(false)
    val saveToAddress: StateFlow<Boolean> = _saveToAddress

    // Update functions
    fun updateName(newName: String) {
        _name.value = newName
    }


    fun updatePhoneNumber(newPhoneNumber: String) {
        _phoneNumber.value = newPhoneNumber
    }

    fun updateStreetName(newStreetName: String) {
        _streetName.value = newStreetName
    }



    fun updateCity(newCity: String) {
        _city.value = newCity
    }


    fun setSaveToFirebase(save: Boolean) {
        _saveToAddress.value = save
    }

    // Function to save the Address object
    fun saveAddress(address: Address) {

        val addressToSave = AddNewAddress(
            address = address
        )
        viewModelScope.launch {
            Log.d("shopifyCustomerID", UserSession.shopifyCustomerID.toString())
            UserSession.shopifyCustomerID?.let { repository.createCustomerAddress(it,addressToSave) }
        }
    }
}
