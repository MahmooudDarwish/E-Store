package com.example.e_store.features.settings.view_model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.CurrencyResponse
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: EStoreRepository) : ViewModel() {


    private val _currencyResponse = MutableStateFlow<DataState<CurrencyResponse>>(DataState.Loading)
    val currencyResponse = _currencyResponse.asStateFlow()

    private val _selectedCurrency = mutableStateOf("Select Currency Type")


    fun fetchCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("CurrencyViewModel", "Fetching currency data")
                repository.fetchConversionRates().collect { response ->
                    _currencyResponse.value = response.let { DataState.Success(it) }
                    UserSession.conversionRates = response.conversion_rates
                  }
            } catch (e: Exception) {
                _currencyResponse.value = DataState.Error(2131820840)

            }

        }
    }

}