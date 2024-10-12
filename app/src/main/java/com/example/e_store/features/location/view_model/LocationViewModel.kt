package com.example.e_store.features.location.view_model

import android.location.Address
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.AppliedDiscount
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DeletionState
import com.example.e_store.utils.shared_models.NavigationHolder
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(val repository: EStoreRepository) : ViewModel() {
    // Simulate a list of saved locations
    private val _locations = MutableStateFlow<DataState<AddressResponse>>(DataState.Loading)

    val locations = _locations.asStateFlow()

    val deletionState = MutableStateFlow<DeletionState>(DeletionState.CanDelete)



    fun deleteLocation(locationId: Long, isDefault: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_locations.value is DataState.Success<AddressResponse> &&
                (_locations.value as DataState.Success<AddressResponse>).data.addresses.size > 1 && !isDefault) {
                UserSession.shopifyCustomerID?.let { repository.deleteCustomerAddress(it, locationId) }
                deletionState.value = DeletionState.CanDelete

                Log.d("LocationViewModel", "Location deleted successfully")
            } else {
                val message = if (isDefault) {
                    "You cannot delete the default location. Please edit it instead."
                } else {
                    "At least one location is required."
                }
                deletionState.value = DeletionState.CannotDelete(message)
                NavigationHolder.id = locationId
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

    fun fetchAllLocations() {
        viewModelScope.launch {
            try {
                UserSession.shopifyCustomerID?.let {
                    repository.fetchCustomerAddresses(it).collect { dataState ->
                        Log.d("LocationViewModel", "DataState received: $dataState")
                        if (dataState != null) {
                            _locations.value = DataState.Success(dataState)
                            Log.d("LocationViewModel", "Locations fetched successfully")
                            Log.d("LocationViewModel", "Locations: $dataState")

                            fetchAllLocations()
                        } else {
                            _locations.value = DataState.Error(R.string.unexpected_error)
                        }

                    }
                }
            } catch (ex: Exception) {
                _locations.value = DataState.Error(R.string.unexpected_error)
                Log.e("LocationViewModel", "Error fetching locations", ex)
            }
        }

    }

    fun makeDefaultLocation(addressId: Long,address: com.example.e_store.utils.shared_models.Address)
    {
        if ( address.default == false)
        {
            address.default=true

            viewModelScope.launch(Dispatchers.IO) {
                UserSession.shopifyCustomerID?.let {
                    repository.updateCustomerAddress(it,addressId,AddNewAddress(address))
                }
            }

        }

    }

}
