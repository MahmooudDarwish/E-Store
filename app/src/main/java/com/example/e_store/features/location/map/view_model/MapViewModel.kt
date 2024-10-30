package com.example.e_store.features.location.map.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.UserAddress
import com.example.e_store.utils.shared_models.UserSession
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class MapViewModel ( val repository : EStoreRepository,val geocoder: Geocoder) : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()
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


    fun  getAddressDetails(location: LatLng, inputValue:String?){
        val addresses: List<android.location.Address>? = geocoder.getFromLocation( location.latitude, location.longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {

            addresses?.let {
                val address = it.firstOrNull()
                if (address != null) {
                    UserAddress.apply {
                        address1 = address.getAddressLine(0)
                        city = address.locality
                        province = address.adminArea
                        country = address.countryName
                        zip = address.postalCode
                        country_code = address.countryCode
                    }

                    saveAddress(
                        Address(
                            address1 = UserAddress.address1,
                            city = UserAddress.city,
                            phone = inputValue,
                            first_name = UserSession.name,
                            country = UserAddress.country,
                            country_code = UserAddress.country_code


                        )
                    )
                }
            }
        }
    }



@SuppressLint("MissingPermission", "SetTextI18n")
fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFetched: (Location?) -> Unit
) {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setMinUpdateIntervalMillis(1000)
        .setMaxUpdateDelayMillis(1000)
        .build()

    fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            onLocationFetched(locationResult.lastLocation)
            fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting the location
        }
    }, null)
}

}