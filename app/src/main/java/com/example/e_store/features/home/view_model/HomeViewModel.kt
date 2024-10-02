package com.example.e_store.features.home.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.DataState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class HomeViewModel(private val repository: EStoreRepository) : ViewModel() {
    private val _brands = MutableStateFlow<DataState<List<Brand>?>>(DataState.Loading)
    val brands = _brands.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errorMessage = when (exception) {
            is IOException -> R.string.network_error
            is HttpException -> R.string.server_error
            is TimeoutException -> R.string.timeout_error
            else -> R.string.unexpected_error
        }
        _brands.value = DataState.Error(errorMessage)
    }
    fun getBrands() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val brands = repository.getBrands()
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
}