package com.example.e_store.features.shopping_cart.view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ShoppingCartViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val _shoppingCartItems =
        MutableStateFlow<DataState<DraftOrderDetails?>>(DataState.Loading)
    val shoppingCartItems = _shoppingCartItems.asStateFlow()

    private val _productStockCount = MutableStateFlow(0)
    private val productStockCount = _productStockCount.asStateFlow()

    private val _applyChangesLoading = MutableStateFlow(false)
    val applyChangesLoading: MutableStateFlow<Boolean> = _applyChangesLoading

    companion object {
        private const val LOG_TAG = "ShoppingCart"
    }

    fun fetchShoppingCartItems(isLoading: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isLoading) toggleLoadingState(true)
                repository.fetchShoppingCartDraftOrders(email = UserSession.email!!)
                    .collect { draftOrderResponse ->
                        if (draftOrderResponse != null) {
                            _shoppingCartItems.value = DataState.Success(draftOrderResponse)
                        } else {
                            _shoppingCartItems.value = DataState.Success(null)
                        }
                    }
            } catch (ex: Exception) {
                _shoppingCartItems.value = DataState.Error(R.string.unexpected_error)
                Log.e("ShoppingCart", "Error fetching shopping cart items", ex)
            }
        }
    }

    fun removeShoppingCartDraftOrder(productId: Long, variantId: Long) {
        applyChangesLoading.value = true
        val existingCustomerDraftOrder = getDraftOrder()

        if (existingCustomerDraftOrder == null) {
            Log.d(LOG_TAG, "No existing draft order found.")
            return
        }

        Log.d(LOG_TAG, "Removing productId: $productId, variantId: $variantId from draft order.")
        val mutableLineItems = existingCustomerDraftOrder.line_items.toMutableList()
        val itemsToRemove =
            mutableLineItems.filter { it.product_id == productId && it.variant_id == variantId.toString() }

        if (mutableLineItems.size == 1) {
            removeEntireDraftOrder(existingCustomerDraftOrder)
        } else if (itemsToRemove.isNotEmpty()) {
            mutableLineItems.removeAll(itemsToRemove)
            existingCustomerDraftOrder.line_items = mutableLineItems
            updateExistingOrder(existingCustomerDraftOrder)
        } else {
            Log.d(LOG_TAG, "No matching items found for removal.")
        }
    }

    private fun removeEntireDraftOrder(draftOrder: DraftOrderDetails) {
        viewModelScope.launch {
            draftOrder.id?.let { id ->
                repository.removeDraftOrder(id)
                fetchShoppingCartItems()
                Log.d(LOG_TAG, "Draft order with ID: $id removed.")
                applyChangesLoading.value = false
            }
        }
    }

    private fun updateExistingOrder(existingCustomerDraftOrder: DraftOrderDetails) {
        viewModelScope.launch {
            existingCustomerDraftOrder.id?.let { id ->
                updateShoppingCartDraftOrder(id, existingCustomerDraftOrder)
                Log.d(LOG_TAG, "Draft order updated after removal with ID: $id.")
            }
        }
    }

    fun getTotalPrice(): Double {
        val draftOrder = (_shoppingCartItems.value as? DataState.Success<DraftOrderDetails?>)?.data
        return draftOrder?.line_items?.sumOf { it.price.toDouble() * it.quantity } ?: 0.0
    }

    private fun updateShoppingCartDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrderDetails: DraftOrderDetails
    ) {
        val shoppingCartDraftOrder = DraftOrderRequest(draft_order = shoppingCartDraftOrderDetails)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
            }.onSuccess {
                fetchShoppingCartItems()
            }.onFailure { ex ->
                Log.e(LOG_TAG, "Error updating shopping cart draft order", ex)
            }.also {
                applyChangesLoading.value = false
            }
        }
    }

    private fun checkStock(item: LineItem, context: Context, action: () -> Unit) {
        val availableStock = productStockCount.value - item.quantity
        when {
            item.quantity < 3 && availableStock > 0 -> action()
            item.quantity >= 3 -> showToast(context, R.string.maximum_quantity_reached)
            else -> showToast(context, R.string.insufficient_stock)
        }
        applyChangesLoading.value = false
    }

    private fun showToast(context: Context, messageResId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, context.getString(messageResId), Toast.LENGTH_SHORT).show()
        }
    }

    fun increaseProductQuantity(productId: Long, variantId: Long, context: Context) {
        applyChangesLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchProduct(productId)
            repository.fetchProduct(productId).collect { product ->
                product.product.variants.find { it.id == variantId }?.let { variant ->
                    _productStockCount.value = variant.inventoryQuantity
                }
                getDraftOrder()?.let { draftOrder ->
                    draftOrder.line_items.forEach { lineItem ->
                        if (lineItem.product_id == productId && lineItem.variant_id == variantId.toString()) {
                            checkStock(context = context, item = lineItem) {
                                lineItem.quantity += 1
                                updateExistingOrder(draftOrder)
                            }
                        }
                    }
                }
            }
        }
    }

    fun decreaseProductQuantity(productId: Long, variantId: Long) {
        applyChangesLoading.value = true
        getDraftOrder()?.let { draftOrder ->
            draftOrder.line_items.forEach { lineItem ->
                if (lineItem.product_id == productId && lineItem.variant_id == variantId.toString()) {
                    lineItem.quantity -= 1
                    updateExistingOrder(draftOrder)
                }
            }
        }
    }

    private fun getDraftOrder(): DraftOrderDetails? {
        return (_shoppingCartItems.value as? DataState.Success<DraftOrderDetails?>)?.data
    }

    private fun toggleLoadingState(isLoading: Boolean) {
        _shoppingCartItems.value = if (isLoading) DataState.Loading else _shoppingCartItems.value
    }
}




