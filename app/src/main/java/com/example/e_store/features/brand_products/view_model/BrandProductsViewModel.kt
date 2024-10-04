package com.example.e_store.features.brand_products.view_model

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

class BrandProductsViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val _brandProducts = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val brandProducts = _brandProducts.asStateFlow()
    fun getBrandProducts(brandID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val brands = repository.fetchBrandProducts(brandId = brandID)
                brands
                    .collect {
                        Log.i("TAG", "get all brands : $it")
                        _brandProducts.value = DataState.Success(it)
                    }

                Log.i("TAG", "get all brands : $brands")
            } catch (ex: Exception) {
                _brandProducts.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

}