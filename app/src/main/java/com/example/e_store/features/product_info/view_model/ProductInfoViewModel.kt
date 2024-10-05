package com.example.e_store.features.product_info.view_model

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.shared_models.ProductDetails
import kotlinx.coroutines.launch

class ProductInfoViewModel : ViewModel() {

    private val product = ProductDetails // Load your product data here.

    // Private mutable state variables
    private val _selectedSize = mutableStateOf("")
    private val _selectedColor = mutableStateOf("")
    private val _price = mutableStateOf("")
    private val _stock = mutableStateOf(0)
    private val _allReviewsVisible = mutableStateOf(false)
    private val _favoriteIcon = mutableStateOf(false)

    // Public read-only state variables
    val selectedSize: State<String> get() = _selectedSize
    val selectedColor: State<String> get() = _selectedColor
    val price: State<String> get() = _price
    val stock: State<Int> get() = _stock
    val allReviewsVisible: State<Boolean> get() = _allReviewsVisible
    val favoriteIcon: State<Boolean> get() = _favoriteIcon

    init {
        // Initialize the ViewModel with the first variant's details.
        if (product.variants.isNotEmpty()) {
            _selectedSize.value = product.variants[0].option1.toString()
            _selectedColor.value = product.variants[0].option2.toString()
            _price.value = product.variants[0].price
            _stock.value = product.variants[0].inventoryQuantity
        }
    }

    fun updateSelectedSize(newSize: String) {
        _selectedSize.value = newSize
        updatePriceAndStock()
    }

    fun updateSelectedColor(newColor: String) {
        _selectedColor.value = newColor
        updatePriceAndStock()
    }

    private fun updatePriceAndStock() {
        val variant = product.variants.find {
            it.option1 == _selectedSize.value && it.option2 == _selectedColor.value
        }
        variant?.let {
            _price.value = it.price
            _stock.value = it.inventoryQuantity
        }
    }

    fun toggleAllReviews() {
        _allReviewsVisible.value = !_allReviewsVisible.value
    }

    fun toggleFavorite() {
        _favoriteIcon.value = !_favoriteIcon.value
    }



}
