package com.example.e_store.features.orders.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Order
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import com.example.e_store.utils.shared_models.UserSession

class OrdersViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val _orders = MutableStateFlow<DataState<List<Order>>>(DataState.Loading)
    val orders = _orders.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errorMessage = when (exception) {
            is IOException -> R.string.network_error
            is HttpException -> R.string.server_error
            is TimeoutException -> R.string.timeout_error
            else -> R.string.unexpected_error
        }
        _orders.value = DataState.Error(errorMessage)

    }


    fun getAllOrders() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val orders = repository.fetchAllOrders(UserSession.email!!)
                orders
                    .collect {
                        Log.i("TAG", "get all brands : $it")
                        _orders.value = DataState.Success(it)
                    }

                Log.i("TAG", "get all brands : $orders")
            } catch (ex: Exception) {
                _orders.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

}