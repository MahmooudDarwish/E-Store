package com.example.e_store.features.shopping_cart.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class ShoppingCartViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val _draftOrderItems =
        MutableStateFlow<DataState<DraftOrderResponse>>(DataState.Loading)

    private val _customerDraftOrders = MutableStateFlow<List<DraftOrderDetails>>(emptyList())


    private val _customerDraftOrdersShoppingCart =
        MutableStateFlow<List<DraftOrderDetails>>(emptyList())

    private val _shoppingCartItems = MutableStateFlow<List<LineItem>>(emptyList())
    val shoppingCartItems = _shoppingCartItems.asStateFlow()

    private  val _shoppingCartItemsCount = MutableStateFlow<Int>(0)
    val shoppingCartItemsCount = _shoppingCartItemsCount.asStateFlow()


    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errorMessage = when (exception) {
            is IOException -> R.string.network_error
            is HttpException -> R.string.server_error
            is TimeoutException -> R.string.timeout_error
            else -> R.string.unexpected_error
        }
        _draftOrderItems.value = DataState.Error(errorMessage)
    }


    fun fetchShoppingCartItems() {
        viewModelScope.launch() {
            try {
                _draftOrderItems.value = DataState.Loading

                repository.fetchAllDraftOrders()
                    .collect { draftOrderResponse ->
                        _draftOrderItems.value = DataState.Success(draftOrderResponse)



                        UserSession.email?.let {
                            filterDraftOrdersByCustomerEmail(
                                draftOrderResponse,
                                it
                            )
                        }
                        Log.d("ShoppingCart", "Draft Orders: ${_draftOrderItems.value}")
                    }
            } catch (ex: Exception) {
            }
        }
    }

    fun filterDraftOrdersByCustomerEmail(draftOrderResponse: DraftOrderResponse, email: String) {
        _customerDraftOrders.value = draftOrderResponse.draft_orders.filter { it.email == email }
        Log.d("ShoppingCart", "Customer Draft Orders: ${_customerDraftOrders.value}")
    }


    fun filterCustomerDraftOrdersType() {
        _customerDraftOrdersShoppingCart.value =
            _customerDraftOrders.value.filter { it.note == "Shopping Cart" }
    }

    fun fetchCustomerShoppingCartDraftOrders() {

        fetchShoppingCartItems()
        viewModelScope.launch()
        {
            delay(4000)
        }

        filterCustomerDraftOrdersType()

        _shoppingCartItems.value =
            _customerDraftOrdersShoppingCart.value.firstOrNull()?.line_items ?: emptyList()



        Log.d("ShoppingCart", "Customer Shopping Cart Draft Orders: ${_shoppingCartItems.value}")
    }


    fun removeShoppingCartDraftOrder(productId: Long, variantId: Long) {

        val existingCustomerDraftOrder = _customerDraftOrdersShoppingCart.value.firstOrNull()


        if (existingCustomerDraftOrder != null) {
            if (existingCustomerDraftOrder.line_items.size == 1) {
                viewModelScope.launch {
                    existingCustomerDraftOrder.id?.let { repository.removeDraftOrder(it) }
                }
            } else {

                if (existingCustomerDraftOrder.line_items.isNotEmpty()) {
                    val existingLineItems = filterCustomerDraftOrdersByProductIDAndVariantID(
                        productId,
                        variantId,
                        existingCustomerDraftOrder,
                    )
                    viewModelScope.launch {
                        delay(2000)
                    }

                    if (existingLineItems.isNotEmpty()) {

                        // Create a mutable copy of line_items
                        val mutableLineItems =
                            existingCustomerDraftOrder.line_items.toMutableList()

                        // Remove the items that match the condition
                        mutableLineItems.removeAll { lineItem ->
                            existingLineItems.any { existingLineItem ->
                                productId == existingLineItem.product_id &&
                                        lineItem.variant_id == existingLineItem.variant_id
                            }
                        }

                        // Update existingDraftOrder.line_items with the new list
                        existingCustomerDraftOrder.line_items = mutableLineItems

                        viewModelScope.launch {
                            delay(2000)
                        }

                        existingCustomerDraftOrder.id?.let {
                            updateShoppingCartDraftOrder(
                                it,
                                existingCustomerDraftOrder
                            )
                        }
                    }

                }
            }
        }
    }


    fun calculateTotalPrice(): Double {
        return _shoppingCartItems.value.sumOf { it.price.toDouble() * it.quantity }
    }

    fun updateShoppingCartDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrderDerails: DraftOrderDetails
    ) {
        val shoppingCartDraftOrder = DraftOrderRequest(
            draft_order = shoppingCartDraftOrderDerails
        )
        Log.d("ShoppingCartUpdate", "Update item to draft order: $shoppingCartDraftOrder")
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                repository.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)

            } catch (ex: Exception) {
            }
        }
    }


    fun fetchProductByID(productId: Long, variantId: Long) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                // Fetch the product details
                val product = repository.fetchProduct(productId).product
                delay(4000) // Simulating a delay for fetching product details

                // Check if the product and variant exist
                product?.variants?.find { it.id == variantId }?.let { variant ->
                    // Update the stock count in the ViewModel
                    _shoppingCartItemsCount.value=variant.inventoryQuantity
                    Log.d("ShoppingCart", "Variant Inventory Quantity: ${variant.inventoryQuantity}")
                }

            } catch (ex: Exception) {
                Log.e("ShoppingCart", "Error fetching product by ID", ex)
            }
        }
    }





    fun filterCustomerDraftOrdersByProductIDAndVariantID(
        productId: Long,
        variantId: Long,
        draftOrders: DraftOrderDetails,
    ): List<LineItem> {

        return draftOrders.line_items.filter { lineItem ->
            Log.d(
                "ShoppingCart",
                "Line Item ID: ${lineItem.product_id} , Variant ID: ${lineItem.variant_id}"
            )
            Log.d("ShoppingCart", "Product ID: $productId , Variant ID: $variantId")
            lineItem.product_id == productId && lineItem.variant_id == variantId.toString()
        }
    }


    fun increaseProductQuantity(productId: Long, variantId: Long) {
        val existingCustomerDraftOrder = _customerDraftOrdersShoppingCart.value.firstOrNull()
        if (existingCustomerDraftOrder != null) {
            val existingLineItems = filterCustomerDraftOrdersByProductIDAndVariantID(
                productId,
                variantId,
                existingCustomerDraftOrder,
            )
            if (existingLineItems.isNotEmpty()) {
                val mutableLineItems = existingCustomerDraftOrder.line_items.toMutableList()
                mutableLineItems.forEach { lineItem ->
                    if (lineItem.product_id == productId && lineItem.variant_id == variantId.toString()) {
                        lineItem.quantity += 1
                    }

                    viewModelScope.launch {
                        delay(2000)
                    }
                    existingCustomerDraftOrder.id?.let {
                        updateShoppingCartDraftOrder(
                            it,
                            existingCustomerDraftOrder
                        )
                    }
                }
            }
        }
    }

    fun decreaseProductQuantity(productId: Long, variantId: Long) {

        val existingCustomerDraftOrder = _customerDraftOrdersShoppingCart.value.firstOrNull()
        if (existingCustomerDraftOrder != null) {
            val existingLineItems = filterCustomerDraftOrdersByProductIDAndVariantID(
                productId,
                variantId,
                existingCustomerDraftOrder,
            )
            if (existingLineItems.isNotEmpty()) {
                val mutableLineItems = existingCustomerDraftOrder.line_items.toMutableList()
                mutableLineItems.forEach { lineItem ->
                    if (lineItem.product_id == productId && lineItem.variant_id == variantId.toString()) {
                        lineItem.quantity -= 1

                    }

                    viewModelScope.launch {
                        delay(2000)
                    }
                    existingCustomerDraftOrder.id?.let {
                        updateShoppingCartDraftOrder(
                            it,
                            existingCustomerDraftOrder
                        )
                    }
                }
            }
        }
    }


    /*
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


       */
}

