package com.example.e_store.features.location.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DeletionState
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LocationViewModel(val repository: EStoreRepository) : ViewModel() {
    // Simulate a list of saved locations
    private val _locations = MutableStateFlow<DataState<AddressResponse>>(DataState.Loading)

    val locations = _locations.asStateFlow()

    val deletionState = MutableStateFlow<DeletionState>(DeletionState.CanDelete)


    fun deleteLocation(locationId: Long, isDefault: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_locations.value is DataState.Success<AddressResponse> &&
                (_locations.value as DataState.Success<AddressResponse>).data.addresses.size > 1 && !isDefault
            ) {
                UserSession.shopifyCustomerID?.let {
                    repository.deleteCustomerAddress(
                        it,
                        locationId
                    )
                    fetchAllLocations()
                }
                deletionState.value = DeletionState.CanDelete

                Log.d("LocationViewModel", "Location deleted successfully")
            } else {
                val message = if (isDefault) {
                    "You cannot delete the default location. Please edit it instead."
                } else {
                    "At least one location is required."
                }
                deletionState.value = DeletionState.CannotDelete(message)
            }
        }
    }


    /*

        fun deleteLocation(locationId: Long, isDefault:Boolean) {
            viewModelScope.launch(Dispatchers.IO) {
                if ((_locations.value as DataState.Success<AddressResponse>).data.addresses.size > 1 &&isDefault==false )
                {
                    UserSession.shopifyCustomerID?.let { repository.deleteCustomerAddress(it, locationId) }
                }else{
                    canNotDelete.value = true
                    NavigationHolder.id=locationId
                }
            }
        }
    */

    private fun fetchShopifyCustomer(email: String) {
        viewModelScope.launch {
            try {
                repository.fetchCustomerByEmail(email).let {
                    UserSession.shopifyCustomerID = it.id
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    429 -> {
                        Log.e("HomeViewModel", "Error 429: Too many requests.")
                    }

                    422 -> {
                        Log.e("HomeViewModel", "Error 422: Unprocessable entity.")
                    }

                    else -> {
                        Log.e("HomeViewModel", "HttpException: ${e.message}")
                    }
                }
            } catch (e: IOException) {
                Log.e("HomeViewModel", "IOException: ${e.message}")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun fetchAllLocations() {
        if (UserSession.shopifyCustomerID == null) {
            fetchShopifyCustomer(email = UserSession.email!!)
        }
        viewModelScope.launch {
            try {
                UserSession.shopifyCustomerID?.let {
                    repository.fetchCustomerAddresses(it).collect { dataState ->
                        Log.d("LocationViewModel", "DataState received: $dataState")
                        _locations.value = DataState.Success(dataState)
                        Log.d("LocationViewModel", "Locations fetched successfully")
                        Log.d("LocationViewModel", "Locations: $dataState")

                    }
                } ?: run {
                    _locations.value = DataState.Error(R.string.default_location)
                    Log.e("LocationViewModel", "Error fetching locations")
                }

            } catch (ex: Exception) {
                _locations.value = DataState.Error(R.string.unexpected_error)
                Log.e("LocationViewModel", "Error fetching locations", ex)
            }
        }

    }

    fun makeDefaultLocation(
        addressId: Long,
        address: com.example.e_store.utils.shared_models.Address,
    ) {
        if (address.default == false) {
            address.default = true

            viewModelScope.launch(Dispatchers.IO) {
                UserSession.shopifyCustomerID?.let {
                    repository.updateCustomerAddress(it, addressId, AddNewAddress(address))
                }
            }

        }

    }

}
