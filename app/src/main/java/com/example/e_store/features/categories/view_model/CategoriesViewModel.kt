package com.example.e_store.features.categories.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.e_store.utils.shared_models.Product

class CategoriesViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val _categoriesProducts = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val categoriesProducts = _categoriesProducts.asStateFlow()


    fun getCategoriesProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = repository.fetchCategoriesProducts()
                products.collect {
                        Log.i("TAG", "get all brands : $it")
                        _categoriesProducts.value = DataState.Success(it)
                    }

                Log.i("TAG", "get all category products : $products")
            } catch (ex: Exception) {
                _categoriesProducts.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

}