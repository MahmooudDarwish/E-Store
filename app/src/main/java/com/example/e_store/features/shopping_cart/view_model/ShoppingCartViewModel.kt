package com.example.e_store.features.shopping_cart.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class ShoppingCartViewModel(private val repository: EStoreRepository) : ViewModel() {
    private val _shoppingCartItems =
        MutableStateFlow<DataState<DraftOrderResponse>>(DataState.Loading)
    val shoppingCartItems = _shoppingCartItems.asStateFlow()




    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errorMessage = when (exception) {
            is IOException -> R.string.network_error
            is HttpException -> R.string.server_error
            is TimeoutException -> R.string.timeout_error
            else -> R.string.unexpected_error
        }
        _shoppingCartItems.value = DataState.Error(errorMessage)
    }



    fun fetchShoppingCartItems() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                _shoppingCartItems.value = DataState.Loading
                UserSession.Uid?.let {
                    repository.fetchAllDraftOrders()
                        .collect {
                            _shoppingCartItems.value = DataState.Success(it)
                        }
                }
            } catch (ex: Exception) {
                _shoppingCartItems.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

    fun  removeShoppingCartDraftOrder(draftOrderId: Long) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                repository.removeDraftOrder(draftOrderId)
            } catch (ex: Exception) {
                _shoppingCartItems.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

    fun updateShoppingCartDraftOrder(draftOrderId: Long, shoppingCartDraftOrder: DraftOrderRequest) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                repository.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
            } catch (ex: Exception) {
                _shoppingCartItems.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }


fun createShoppingCartDraftOrder (shoppingCartDraftOrder: DraftOrderRequest) {
    viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
        try {
            repository.createDraftOrder(shoppingCartDraftOrder)
        } catch (ex: Exception) {
            _shoppingCartItems.value = DataState.Error(R.string.unexpected_error)
        }
    }
}



}

