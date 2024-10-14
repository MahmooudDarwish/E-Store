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
import retrofit2.HttpException
import java.io.IOException

class MapViewModel ( val repository : EStoreRepository) : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()


    private   val  _isAddressExist = MutableStateFlow(false)
    val isAddressExist = _isAddressExist.asStateFlow()

    private  val  _isPhoneExist = MutableStateFlow(false)
    val isPhoneExist = _isPhoneExist.asStateFlow()

    private val _address = MutableStateFlow<List<Address?>>(emptyList())
    val address=_address.asStateFlow()



    fun saveAddress(address: Address) {
        val addressToSave = AddNewAddress(
            address = address
        )
        viewModelScope.launch {
            try {
                UserSession.shopifyCustomerID?.let {
                    repository.createCustomerAddress(it, addressToSave)
                    Log.d("MapViewModel", "Address saved: $address")
                    Log.d("MapViewModel", "Customer ID: ${UserSession.shopifyCustomerID}")
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    429 -> {
                        _errorMessage.value = "Too many requests, please try again later."
                        Log.e("MapViewModel", "Error 429: Too many requests.")
                    }
                    422 -> {
                        _errorMessage.value = "Unprocessable entity, please check your input."
                        Log.e("MapViewModel", "Error 422: Unprocessable entity.")
                    }
                    else -> {
                        _errorMessage.value = "An unexpected error occurred, please try again later."
                        Log.e("MapViewModel", "HttpException: ${e.message}")
                    }
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error, please check your connection."
                Log.e("MapViewModel", "IOException: ${e.message}")
            } catch (e: Exception) {
                _errorMessage.value = "An unexpected error occurred."
                Log.e("MapViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun fetchCustomerAddresses() {
        viewModelScope.launch {
            try {
                UserSession.shopifyCustomerID?.let {
                    repository.fetchCustomerAddresses(it).collect { addresses ->
                        _address.value = addresses.addresses
                        Log.d("MapViewModel", "fetchCustomerAddresses: ${_address.value}")
                        Log.d("MapViewModel", "fetchCustomerAddresses: ${UserSession.shopifyCustomerID}")

                        UserSession.phone?.let { phone -> isPhoneExistFUN(phone) }
                    }
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    429 -> {
                        _errorMessage.value = "Too many requests, please try again later."
                        Log.e("MapViewModel", "Error 429: Too many requests.")
                    }
                    422 -> {
                        _errorMessage.value = "Unprocessable entity, please check your input."
                        Log.e("MapViewModel", "Error 422: Unprocessable entity.")
                    }
                    else -> {
                        _errorMessage.value = "An unexpected error occurred, please try again later."
                        Log.e("MapViewModel", "HttpException: ${e.message}")
                    }
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error, please check your connection."
                Log.e("MapViewModel", "IOException: ${e.message}")
            } catch (e: Exception) {
                _errorMessage.value = "An unexpected error occurred."
                Log.e("MapViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun isPhoneExistFUN(phoneNumber: String) {
        try {
            if (phoneNumber.isNotEmpty()) {
                Log.d("MapViewModel", "isPhoneExistFUN: $phoneNumber")

                _isPhoneExist.value = _address.value.any { it?.phone == phoneNumber }
                Log.d("MapViewModel", "isPhoneExistFUN: ${_isPhoneExist.value}")
            } else {
                _isPhoneExist.value = false
            }
        } catch (e: Exception) {
            _errorMessage.value = "An unexpected error occurred while checking the phone number."
            Log.e("MapViewModel", "Exception in isPhoneExistFUN: ${e.message}")
        }
    }
}


/*
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
*/


