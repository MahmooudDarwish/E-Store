package com.example.e_store.features.search.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SearchViewModel(private val repository: EStoreRepository) : ViewModel() {


    private val _products = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val products = _products.asStateFlow()



    fun fetchAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = repository.fetchProducts()
                products
                    .collect {
                        _products.value = DataState.Success(it)
                    }

            } catch (ex: Exception) {
                _products.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }




}