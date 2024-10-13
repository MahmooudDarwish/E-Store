package com.example.e_store.features.home.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.Customer
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll

class HomeViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val _brands = MutableStateFlow<DataState<List<Brand>>>(DataState.Loading)
    val brands = _brands.asStateFlow()

    private val _customer = MutableStateFlow<Customer?>(null)
    val customer = _customer.asStateFlow()

    private val _discountCodes =
        MutableStateFlow<DataState<List<DiscountCodesResponse?>>>(DataState.Loading)
    val discountCodes = _discountCodes.asStateFlow()

    private val _forUProducts = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val forUProducts = _forUProducts.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    fun refreshAllData() {
        _isRefreshing.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                coroutineScope {
                    val brandsJob = launch { getBrands() }
                    val forUProductsJob = launch { getForUProducts() }
                    val discountCodesJob = launch { fetchDiscountCodes() }

                    delay(500)  //to show the animation xDD

                    joinAll(brandsJob, forUProductsJob, discountCodesJob)
                }
                } catch (ex: Exception) {
                Log.e("TAG", "Error refreshing data: ${ex.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errorMessage = when (exception) {
            is IOException -> R.string.network_error
            is HttpException -> R.string.server_error
            is TimeoutException -> R.string.timeout_error
            else -> R.string.unexpected_error
        }
        _brands.value = DataState.Error(errorMessage)
        _isRefreshing.value = false // Ensure refreshing is turned off after an error

    }

    fun getBrands() {
        _brands.value = DataState.Loading
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val brands = repository.fetchBrands()
                brands
                    .collect {
                        Log.i("TAG", "get all brands : $it")
                        _brands.value = DataState.Success(it)
                    }

                Log.i("TAG", "get all brands : $brands")
            } catch (ex: Exception) {
                _brands.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

    fun getForUProducts() {
        _forUProducts.value = DataState.Loading

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val brands = repository.fetchForUProducts()
                brands
                    .collect {
                        Log.i("TAG", "get all brands : $it")
                        _forUProducts.value = DataState.Success(it)
                    }

                Log.i("TAG", "get all brands : $brands")
            } catch (ex: Exception) {
                _forUProducts.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

    fun fetchDiscountCodes() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                _discountCodes.value = DataState.Loading
                val discountCodes = repository.fetchDiscountCodes()
                discountCodes
                    .collect {
                        Log.i("HomeViewModel", "get all discountCodes : $it")
                        _discountCodes.value = DataState.Success(it)
                    }

            } catch (ex: Exception) {
                _discountCodes.value = DataState.Error(R.string.unexpected_error)
            }


        }
    }

    fun fetchShopifyCustomer(email: String) {
        viewModelScope.launch {
            repository.fetchCustomerByEmail(email).let {
                _customer.value = it
                Log.i("HomeViewModel", "get all customer : $it")
                    UserSession.shopifyCustomerID = it.id
                Log.i("HomeViewModel", "get all customer : ${UserSession.shopifyCustomerID}")

            }
        }
    }

    fun fetchCurrency() {
        viewModelScope.launch {
            try {
                repository.fetchConversionRates().collect { response ->
                    UserSession.conversionRates = response.conversion_rates
                }
            } catch (e: Exception) {
                DataState.Error(R.string.network_error)

            }
        }


    }
}