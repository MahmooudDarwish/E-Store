package com.example.e_store.features.product_info.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_methods.convertCurrency
import com.example.e_store.utils.shared_methods.initializeProductDetails
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.ProductDetails
import com.example.e_store.utils.shared_models.SingleProductResponse
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class ProductInfoViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val product = ProductDetails // Load your product data here.

    // Private mutable state variables
    private val _selectedSize = mutableStateOf("")
    private val _selectedColor = mutableStateOf("")
    private val _price = mutableStateOf("")
    private val _stock = mutableStateOf(0)
    private val _allReviewsVisible = mutableStateOf(false)
    private val _favoriteIcon = mutableStateOf(false)
    private val _productState =
        MutableStateFlow<DataState<SingleProductResponse>>(DataState.Loading)
    val productState = _productState.asStateFlow()

    // Public read-only state variables
    val selectedSize: State<String> get() = _selectedSize
    val selectedColor: State<String> get() = _selectedColor
    val price: State<String> get() = _price
    val stock: State<Int> get() = _stock
    val allReviewsVisible: State<Boolean> get() = _allReviewsVisible
    val favoriteIcon: State<Boolean> get() = _favoriteIcon

    // Draft orders
    private val _draftOrderItems =
        MutableStateFlow<DataState<DraftOrderResponse>>(DataState.Loading)
    private val _customerDraftOrders = MutableStateFlow<List<DraftOrderDetails>>(emptyList())
    private val _customerDraftOrdersFavorite =
        MutableStateFlow<List<DraftOrderDetails>>(emptyList())
    private val _customerDraftOrdersShoppingCart =
        MutableStateFlow<List<DraftOrderDetails>>(emptyList())

    init {
        // Initialize the ViewModel with the first variant's details.
        if (product.variants.isNotEmpty()) {
            _selectedSize.value = product.variants[0].option1.toString()
            _selectedColor.value = product.variants[0].option2.toString()
            _price.value = product.variants[0].price
            _stock.value = product.variants[0].inventoryQuantity
        }
    }

    // Fetch product details
    fun fetchProductById() {
        viewModelScope.launch {
            try {
                repository.fetchProduct(ProductDetails.id)
                    .flowOn(Dispatchers.IO)
                    .collectLatest { product ->
                        _productState.value = DataState.Success(product)
                        initializeProductDetails(product.product)

                    }
            } catch (ex: Exception) {
                _productState.value = DataState.Error(R.string.unexpected_error)
            }
        }
    }

    // Update selected size or color and adjust price and stock accordingly
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
            _price.value = convertCurrency(it.price.toDouble())
            _stock.value = it.inventoryQuantity
        }
    }

    // Toggle reviews visibility
    fun toggleAllReviews() {
        _allReviewsVisible.value = !_allReviewsVisible.value
    }

    // Toggle favorite status
    fun toggleFavorite() {
        _favoriteIcon.value = !_favoriteIcon.value
    }

    // Fetch all draft orders and filter by customer
    fun fetchAllDraftOrders(isStart: Boolean = false) {
        viewModelScope.launch {
            repository.fetchAllDraftOrders().collect { draftOrderResponse ->
                _draftOrderItems.value = DataState.Success(draftOrderResponse)

                UserSession.email?.let {
                    filterDraftOrdersByCustomerEmail(draftOrderResponse, it)
                    if (isStart) {
                        checkIfItemIsInFavouriteDraftOrder(
                            ProductDetails.id,
                            ProductDetails.variants.first().id
                        )
                    }
                }
            }
        }
    }

    private fun filterDraftOrdersByCustomerEmail(
        draftOrderResponse: DraftOrderResponse,
        email: String,
    ) {
        _customerDraftOrders.value = draftOrderResponse.draft_orders.filter { it.email == email }
    }

    private fun filterCustomerDraftOrdersByProductIDAndVariantID(
        productId: Long,
        variantId: Long,
        draftOrders: DraftOrderDetails,
    ): List<LineItem> {
        return draftOrders.line_items.filter {
            it.product_id == productId && it.variant_id == variantId.toString()
        }
    }

    private fun filterCustomerDraftOrdersType(isFavorite: Boolean) {
        when (isFavorite) {
            true -> _customerDraftOrdersFavorite.value = _customerDraftOrders.value.filter {
                it.note == "Favourite" && it.email == UserSession.email
            }

            false -> _customerDraftOrdersShoppingCart.value = _customerDraftOrders.value.filter {
                it.note == "Shopping Cart"
            }
        }
    }

    // Create a draft order
    fun createDraftOrder(
        lineItemList: MutableList<LineItem>,
        isFavorite: Boolean,
        productID: Long,
    ) {
        val note = if (isFavorite) "Favourite" else "Shopping Cart"
        fetchAllDraftOrders()
        filterCustomerDraftOrdersType(isFavorite)

        try {
            val draftOrders =
                if (isFavorite) _customerDraftOrdersFavorite.value else _customerDraftOrdersShoppingCart.value

            if (draftOrders.isNotEmpty()) {
                val existingDraftOrder = draftOrders.first()
                val existingLineItems = filterCustomerDraftOrdersByProductIDAndVariantID(
                    productID,
                    lineItemList.first().variant_id.toLong(),
                    existingDraftOrder
                )

                if (existingLineItems.isNotEmpty()) {
                    val mutableLineItems = existingDraftOrder.line_items.toMutableList()
                    mutableLineItems.removeAll { lineItem ->
                        existingLineItems.any { existingLineItem ->
                            productID == existingLineItem.product_id && lineItem.variant_id == existingLineItem.variant_id
                        }
                    }
                    existingDraftOrder.line_items = mutableLineItems
                }

                lineItemList.addAll(existingDraftOrder.line_items)

                viewModelScope.launch {
                    existingDraftOrder.id?.let {
                        repository.updateDraftOrder(
                            it,
                            DraftOrderRequest(
                                DraftOrderDetails(
                                    email = UserSession.email!!,
                                    line_items = lineItemList,
                                    note = note
                                )
                            )
                        )
                    }
                }
            } else {
                val cartDraftOrder = UserSession.email?.let {
                    DraftOrderDetails(
                        email = it,
                        line_items = lineItemList,
                        note = note,
                        tags = null
                    )
                }?.let {
                    DraftOrderRequest(draft_order = it)
                }

                viewModelScope.launch {
                    cartDraftOrder?.let {
                        repository.createDraftOrder(it)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CreateDraftOrder", "Error creating draft order: ${e.message}")
        }
    }

    // Remove an item from the shopping cart
    fun removeShoppingCartDraftOrder(productId: Long, variantId: Long) {
        viewModelScope.launch {
            repository.fetchAllDraftOrders().collect { draftOrderResponse ->
                _draftOrderItems.value = DataState.Success(draftOrderResponse)

                UserSession.email?.let {
                    filterDraftOrdersByCustomerEmail(draftOrderResponse, it)
                }

                filterCustomerDraftOrdersType(true)
                val existingCustomerDraftOrder = _customerDraftOrdersFavorite.value.firstOrNull()

                existingCustomerDraftOrder?.let {
                    val existingLineItems =
                        filterCustomerDraftOrdersByProductIDAndVariantID(productId, variantId, it)

                    if (existingLineItems.isNotEmpty()) {
                        val mutableLineItems = it.line_items.toMutableList()
                        mutableLineItems.removeAll { lineItem ->
                            existingLineItems.any { existingLineItem ->
                                productId == existingLineItem.product_id && lineItem.variant_id == existingLineItem.variant_id
                            }
                        }

                        if (mutableLineItems.isEmpty()) {
                            deleteDraftOrder(it.id!!)
                        } else {
                            it.line_items = mutableLineItems
                            updateShoppingCartDraftOrder(it.id!!, it)
                        }
                    }
                }
            }

        }

    }

    private fun deleteDraftOrder(draftOrderId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeDraftOrder(draftOrderId)
            } catch (ex: Exception) {
                Log.e("ShoppingCartUpdate", "Failed to delete draft order: ${ex.message}")
            }
        }
    }

    private fun updateShoppingCartDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrderDetails: DraftOrderDetails,
    ) {
        val shoppingCartDraftOrder = DraftOrderRequest(draft_order = shoppingCartDraftOrderDetails)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
                fetchAllDraftOrders()
            } catch (ex: Exception) {
                Log.e("ShoppingCartUpdate", "Failed to update draft order: ${ex.message}")
            }
        }
    }

    // Check if item is in favorite draft order
    private fun checkIfItemIsInFavouriteDraftOrder(productId: Long, variantId: Long) {
        val favoriteDraftOrder = _customerDraftOrders.value.find { it.note == "Favourite" }

        val isItemInFavorites = favoriteDraftOrder?.line_items?.any {
            it.product_id == productId && it.variant_id == variantId.toString()
        }

        _favoriteIcon.value = isItemInFavorites ?: false
    }
}








