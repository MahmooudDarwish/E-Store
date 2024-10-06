package com.example.e_store.features.product_info.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.ProductDetails
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch



class ProductInfoViewModel(private val repository: EStoreRepository) : ViewModel() {

    init {
        fetchAllDraftOrders()
    }
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


    private val _draftOrderItems =
        MutableStateFlow<DataState<DraftOrderResponse>>(DataState.Loading)
    private val _customerDraftOrders = MutableStateFlow<List<DraftOrderDetails>>(emptyList())

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


    fun fetchAllDraftOrders() {
        viewModelScope.launch {
            repository.fetchAllDraftOrders().collect { draftOrderResponse ->
                // Update the state with fetched draft orders
                _draftOrderItems.value = DataState.Success(draftOrderResponse)
                Log.d("ProductInfoViewModel", "Draft Orders: $draftOrderResponse")

                // Filter the draft orders based on the specific email
                UserSession.email?.let {
                    filterDraftOrdersByCustomerEmail(
                        draftOrderResponse,
                        it
                    )

                    Log.d(
                        "ProductInfoViewModel",
                        "Filtered Draft Orders for ${UserSession.email}: ${_customerDraftOrders.value}"
                    )
                }
            }
        }
    }

    fun filterDraftOrdersByCustomerEmail(draftOrderResponse: DraftOrderResponse, email: String) {
        _customerDraftOrders.value = draftOrderResponse.draft_orders.filter { it.email == email }
    }

    fun filterCustomerDraftOrdersByProductIDAndVariantID(
        productId: Long,
        variantId: Long,
        draftOrders: DraftOrderDetails,
    ): List<LineItem> {


        return draftOrders.line_items.filter { lineItem ->
            Log.d("ProductInfoViewModel22", "Line Item ID: ${lineItem.id} , Variant ID: ${lineItem.variant_id}")
            Log.d("ProductInfoViewModel22", "Product ID: $productId , Variant ID: $variantId")
            lineItem.product_id == productId && lineItem.variant_id == variantId.toString()

        }

    }

    fun filterCustomerDraftOrdersType(isFavorite: Boolean)
    {

    }



    fun createDraftOrder(lineItemList: MutableList<LineItem>, isFavorite: Boolean,productID :Long) {
        val note = if (isFavorite) "Favorite" else "Shopping Cart"
        fetchAllDraftOrders()

        viewModelScope.launch {
            delay(4000)
        }

        if (_customerDraftOrders.value.isNotEmpty()) {

            val existingDraftOrder = _customerDraftOrders.value.first()
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
                            productID == existingLineItem.product_id &&
                                    lineItem.variant_id == existingLineItem.variant_id
                        }
                    }

                    // Update existingDraftOrder.line_items with the new list
                    existingDraftOrder.line_items = mutableLineItems
                }


            }
            Log.d("existingDraftOrder", "Existing Draft Order: $existingDraftOrder")






            lineItemList.addAll( existingDraftOrder.line_items)



            val cartDraftOrder = UserSession.email?.let {
                DraftOrderDetails(
                    email = it,
                    line_items = lineItemList,
                    note = note,
                    tags = null

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


        }else{

            val cartDraftOrder = UserSession.email?.let {
                DraftOrderDetails(
                    email = it,
                    line_items = lineItemList,
                    note = note,
                    tags = null

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




        //_cartItems.add(newItem)
    }


}

private fun <E> MutableList<E>.addAll(elements: List<E?>) {
    elements.forEach { element ->
        if (element != null) {
            this.add(element)
        }
    }
}





