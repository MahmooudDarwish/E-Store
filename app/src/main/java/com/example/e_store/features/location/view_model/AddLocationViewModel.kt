package com.example.e_store.features.location.view_model

import android.provider.ContactsContract.Contacts.Data
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.CountryInfo
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.GeoNameLocation
import com.example.e_store.utils.shared_models.NavigationHolder
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddLocationViewModel(val repository: EStoreRepository) : ViewModel() {

    // MutableStateFlow for each field
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _countries = MutableStateFlow<DataState<List<CountryInfo?>>>(DataState.Loading)
    val countries = _countries.asStateFlow()

    private val _cities = MutableStateFlow<DataState<List<GeoNameLocation?>>>(DataState.Loading)
    val cities = _cities.asStateFlow()

    private val _selectedCountry = MutableStateFlow<CountryInfo?>(null)
    val selectedCountry = _selectedCountry.asStateFlow()

    private val _selectedCity = MutableStateFlow<GeoNameLocation?>(null)
    val selectedCity = _selectedCity.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _streetName = MutableStateFlow("")
    val streetName: StateFlow<String> = _streetName

    private val _country = MutableStateFlow("")
    val country: StateFlow<String> = _country

    private val _countryCode = MutableStateFlow("")
    val countryCode: StateFlow<String> = _countryCode


    private val _city = MutableStateFlow<String?>(null)
    val city: StateFlow<String?> = _city

    private val _saveToAddress = MutableStateFlow(false)
    val saveToAddress: StateFlow<Boolean> = _saveToAddress

    private val _isAddressExist = MutableStateFlow(false)
    val isAddressExist = _isAddressExist.asStateFlow()

    private val _isPhoneExist = MutableStateFlow(false)


    val isPhoneExist = _isPhoneExist.asStateFlow()

    private val _address = MutableStateFlow<List<Address?>>(emptyList())
    val address = _address.asStateFlow()

    private val _selectedAddress = MutableStateFlow<DataState<Address?>>(DataState.Loading)
    val selectedAddress = _selectedAddress.asStateFlow()


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

    fun updateCountry(newCountry: String) {
        _country.value = newCountry
    }

    fun updateCity(newCity: String?) {
        _city.value = newCity
    }
    fun updateCountryCode(newCountryCode: String) {
        _countryCode.value = newCountryCode
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
            UserSession.shopifyCustomerID?.let {
                repository.createCustomerAddress(
                    it,
                    addressToSave
                )
            }
        }
    }

    fun fetchCustomerAddresses() {
        viewModelScope.launch {
            UserSession.shopifyCustomerID?.let {
                repository.fetchCustomerAddresses(it).collect { addresses ->
                    _address.value = addresses.addresses
                    Log.d("AddLocationViewModel", "fetchCustomerAddresses: ${_address.value}")
                    Log.d(
                        "AddLocationViewModel",
                        "fetchCustomerAddresses: ${UserSession.shopifyCustomerID}"
                    )
                }
            }
        }
    }

    // Check if an address exists in the fetched addresses
    fun isAddressExistFUN(addressLine: String) {
        return if (addressLine.isNotEmpty()) {
            Log.d("AddLocationViewModel", "isAddressExistFUN: $addressLine")

            // Assuming _address.value is a list of Address objects with a property 'addressLine'
            _isAddressExist.value = _address.value.any { it?.address1 == addressLine }

        } else {
            _isAddressExist.value = false
        }
    }


    // Check if a phone number exists in the fetched addresses
    fun isPhoneExistFUN(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            Log.d("AddLocationViewModel", "isPhoneExistFUN: $phoneNumber")

            // Assuming _address.value is a list of Address objects with a property 'phoneNumber'
            _isPhoneExist.value = _address.value.any { (it?.phone == phoneNumber) }
            Log.d("AddLocationViewModel", "isPhoneExistFUN: ${_isPhoneExist.value}")

        } else {
            _isPhoneExist.value = false
        }

    }


    fun updateDefaultLocation(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            UserSession.shopifyCustomerID?.let {
                NavigationHolder.id?.let { it1 ->
                    repository.updateCustomerAddress(
                        it,
                        it1, AddNewAddress(address)
                    )
                }
            }
        }
    }

    fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCountries().collect { countryList ->
                _countries.value = DataState.Success(countryList)
            }
        }
    }

    fun getCities(countryCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCitiesByCountry(countryCode).collect { cityList ->
                _cities.value = DataState.Success(cityList)
                Log.d("getCities", "getCities: $cityList")
            }
        }
    }

    fun selectCountry(country: CountryInfo) {
        _selectedCountry.value = country
        _countryCode.value= country.countryCode
        Log.d("country_code", "LocationScreen: ${country.countryCode}")

        getCities(country.countryCode)
    }

    fun selectCity(city: GeoNameLocation?) {
        _selectedCity.value = city
    }


}



