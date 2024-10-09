package com.example.e_store.features.product_info.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
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
    private val _productState = MutableStateFlow<DataState<SingleProductResponse>>(DataState.Loading)
    val productState= _productState.asStateFlow()

    // Public read-only state variables
    val selectedSize: State<String> get() = _selectedSize
    val selectedColor: State<String> get() = _selectedColor
    val price: State<String> get() = _price
    val stock: State<Int> get() = _stock
    val allReviewsVisible: State<Boolean> get() = _allReviewsVisible
    val favoriteIcon: State<Boolean> get() = _favoriteIcon


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

    fun fetchProductById() {

        viewModelScope.launch {
            try {
                repository.fetchProduct(ProductDetails.id)
                    .flowOn(Dispatchers.IO) // Specify the dispatcher for the flow's upstream
                    .collectLatest { product ->
                        Log.d("ProductInfoViewModel", "Product: $product")
                        delay(200) // Simulating a delay for demonstration purposes
                        _productState.value = DataState.Success(product)
                        initializeProductDetails(product.product)
                    }
            } catch (ex: Exception) {
                // Log the error for debugging purposes
                Log.e("ProductInfoViewModel", "Error fetching product", ex)
                _productState.value = DataState.Error(R.string.unexpected_error)
            }
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


    fun fetchAllDraftOrders(isStart: Boolean = false) {
        viewModelScope.launch {
            repository.fetchAllDraftOrders().collect { draftOrderResponse ->
                _draftOrderItems.value = DataState.Success(draftOrderResponse)
                Log.d("ProductInfoViewModel", "Draft Orders: $draftOrderResponse")

                UserSession.email?.let {
                    filterDraftOrdersByCustomerEmail(
                        draftOrderResponse, it
                    )
                    if (isStart){
                        checkIfItemIsInFavouriteDraftOrder(ProductDetails.id, ProductDetails.variants.first().id)
                        }

                    Log.d(
                        "ProductInfoViewModel",
                        "Filtered Draft Orders for ${UserSession.email}: ${_customerDraftOrders.value}"
                    )
                }
            }
        }
    }

    private fun filterDraftOrdersByCustomerEmail(
        draftOrderResponse: DraftOrderResponse, email: String
    ) {
        _customerDraftOrders.value = draftOrderResponse.draft_orders.filter { it.email == email }
    }

    private fun filterCustomerDraftOrdersByProductIDAndVariantID(
        productId: Long,
        variantId: Long,
        draftOrders: DraftOrderDetails,
    ): List<LineItem> {


        return draftOrders.line_items.filter { lineItem ->
            Log.d(
                "ProductInfoViewModel22",
                "Line Item ID: ${lineItem.id} , Variant ID: ${lineItem.variant_id}"
            )
            Log.d("ProductInfoViewModel22", "Product ID: $productId , Variant ID: $variantId")
            lineItem.product_id == productId && lineItem.variant_id == variantId.toString()

        }

    }

    private fun filterCustomerDraftOrdersType(isFavorite: Boolean) {

        when (isFavorite) {
            true -> {
                _customerDraftOrdersFavorite.value = _customerDraftOrders.value.filter {
                    it.note == "Favourite" && it.email == UserSession.email
                }
            }

            false -> {
                _customerDraftOrdersShoppingCart.value =
                    _customerDraftOrders.value.filter { it.note == "Shopping Cart" }
            }
        }
    }


    fun createDraftOrder(
        lineItemList: MutableList<LineItem>, isFavorite: Boolean, productID: Long
    ) {


        val note = if (isFavorite) "Favourite" else "Shopping Cart"
        fetchAllDraftOrders()
        filterCustomerDraftOrdersType(isFavorite)

        try {
            // Call your repository method to create a draft order

            // Handle success, e.g., updating state or showing a message

            viewModelScope.launch {
                delay(4000)
            }

            val draftOrders = when (isFavorite) {
                true -> {
                    _customerDraftOrdersFavorite.value
                }

                false -> {
                    _customerDraftOrdersShoppingCart.value
                }
            }

            if (draftOrders.isNotEmpty()) {
                val existingDraftOrder = draftOrders.first()
                if (existingDraftOrder.line_items.isNotEmpty()) {
                    val existingLineItems = filterCustomerDraftOrdersByProductIDAndVariantID(
                        productID,
                        lineItemList.first().variant_id.toLong(),
                        existingDraftOrder,
                    )

                    Log.d("existingDraftOrder", "Existing Line Items: $existingDraftOrder")

                    if (existingLineItems.isNotEmpty()) {
                        Log.d("ProductInfoViewModel22", "Existing Line Items: $existingLineItems")

                        // Create a mutable copy of line_items
                        val mutableLineItems = existingDraftOrder.line_items.toMutableList()

                        // Remove the items that match the condition
                        mutableLineItems.removeAll { lineItem ->
                            existingLineItems.any { existingLineItem ->
                                productID == existingLineItem.product_id && lineItem.variant_id == existingLineItem.variant_id
                            }
                        }

                        // Update existingDraftOrder.line_items with the new list
                        existingDraftOrder.line_items = mutableLineItems
                    }


                }
                Log.d("existingDraftOrder", "Existing Draft Order: $existingDraftOrder")






                lineItemList.addAll(existingDraftOrder.line_items)


                val cartDraftOrder = UserSession.email?.let {
                    DraftOrderDetails(
                        email = it, line_items = lineItemList, note = note, tags = null

                    )
                }?.let {
                    DraftOrderRequest(
                        draft_order = it
                    )
                }
                viewModelScope.launch {
                    existingDraftOrder.id?.let {
                        if (cartDraftOrder != null) {
                            repository.updateDraftOrder(it, cartDraftOrder)
                        }

                    }
                }
                Log.d("ProductInfoViewModel", "Update item to draft order: $lineItemList")


            } else {

                val cartDraftOrder = UserSession.email?.let {
                    DraftOrderDetails(
                        email = it, line_items = lineItemList, note = note, tags = null

                    )
                }?.let {
                    DraftOrderRequest(
                        draft_order = it
                    )
                }
                viewModelScope.launch {

                    if (cartDraftOrder != null) {
                        repository.createDraftOrder(cartDraftOrder)
                    }

                }
                Log.d("ProductInfoViewModel", "Adding item to draft order: $lineItemList")
            }
        } catch (e: Exception) {
            // Handle error, e.g., show an error message
            Log.e("CreateDraftOrder", "Error creating draft order: ${e.message}")
        }


        //_cartItems.add(newItem)
    }

    private fun checkIfItemIsInFavouriteDraftOrder(productId: Long, variantId: Long) {

        val favoriteDraftOrder = _customerDraftOrders.value.find { draftOrder ->
            draftOrder.note == "Favourite"
        }

        if (favoriteDraftOrder == null) {
            Log.d("checkIfItemIsInFavouriteDraftOrder", "No favorite draft order found.")
            _favoriteIcon.value = false
        }

        val isItemInFavorites = favoriteDraftOrder?.line_items?.any { lineItem ->
            lineItem.product_id == productId && lineItem.variant_id == variantId.toString()
        }

        Log.d("checkIfItemIsInFavouriteDraftOrder", "Item in favorite: $isItemInFavorites")

        if (isItemInFavorites != null) {
            _favoriteIcon.value = isItemInFavorites
        }
    }

    fun removeShoppingCartDraftOrder(productId: Long, variantId: Long) {

        filterCustomerDraftOrdersType(true)
        val existingCustomerDraftOrder = _customerDraftOrdersFavorite.value.firstOrNull()

        Log.d("ShoppingCartUpdate", "Existing Draft Order: $existingCustomerDraftOrder")
        if (existingCustomerDraftOrder != null) {
            if (existingCustomerDraftOrder.line_items.isNotEmpty()) {
                val existingLineItems = existingCustomerDraftOrder.let {
                    filterCustomerDraftOrdersByProductIDAndVariantID(
                        productId,
                        variantId,
                        it,
                    )
                }
                viewModelScope.launch {
                    delay(2000)
                }

                if (existingLineItems.isNotEmpty()) {

                    // Create a mutable copy of line_items
                    val mutableLineItems = existingCustomerDraftOrder.line_items.toMutableList()

                    // Remove the items that match the condition
                    mutableLineItems.removeAll { lineItem ->
                        existingLineItems.any { existingLineItem ->
                            productId == existingLineItem.product_id && lineItem.variant_id == existingLineItem.variant_id
                        }
                    }

                    // If no line items are left after removal, delete the draft order
                    if (mutableLineItems.isEmpty()) {
                        // If it's the last item, delete the entire draft order
                        existingCustomerDraftOrder.id?.let {
                            deleteDraftOrder(it)
                        }
                        Log.d("ShoppingCartUpdate", "Draft order deleted as no items were left.")
                    } else {
                        // Update existingDraftOrder.line_items with the new list
                        existingCustomerDraftOrder.line_items = mutableLineItems

                        viewModelScope.launch {
                            delay(2000)
                        }

                        // Update the draft order with the remaining items
                        existingCustomerDraftOrder.id?.let {
                            updateShoppingCartDraftOrder(it, existingCustomerDraftOrder)
                        }
                        Log.d("ShoppingCartUpdate", "Draft order updated with remaining items.")
                    }
                }
            }
        }
    }

    private fun deleteDraftOrder(draftOrderId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeDraftOrder(draftOrderId)
                Log.d("ShoppingCartUpdate", "Draft order with ID $draftOrderId has been deleted.")
            } catch (ex: Exception) {
                Log.e("ShoppingCartUpdate", "Failed to delete draft order: ${ex.message}")
            }
        }
    }

    private fun updateShoppingCartDraftOrder(
        draftOrderId: Long, shoppingCartDraftOrderDerails: DraftOrderDetails
    ) {
        val shoppingCartDraftOrder = DraftOrderRequest(
            draft_order = shoppingCartDraftOrderDerails
        )
        Log.d("ShoppingCartUpdate", "Update item to draft order: $shoppingCartDraftOrder")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
                Log.d("ShoppingCartUpdate", "Draft order with ID $draftOrderId has been updated.")
                fetchAllDraftOrders()
            } catch (ex: Exception) {
                Log.e("ShoppingCartUpdate", "Failed to update draft order: ${ex.message}")
            }
        }
    }

    fun refreshProductDetails() {

    }
}


private fun <E> MutableList<E>.addAll(elements: List<E?>) {
    elements.forEach { element ->
        if (element != null) {
            this.add(element)
        }
    }
}






