package com.example.e_store.utils.shared_view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.LineItem
import com.example.e_store.utils.shared_models.ProductDetails
import com.example.e_store.utils.shared_models.Property
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteControllerViewModel(private val repository: EStoreRepository) : ViewModel() {


    private val _draftOrderItems =
        MutableStateFlow<DataState<DraftOrderResponse>>(DataState.Loading)
    val draftOrderItems = _draftOrderItems.asStateFlow()

    private val _favourites = MutableStateFlow<List<DraftOrderDetails>>(emptyList())
    val favourites = _favourites.asStateFlow()

    private val _isLoadingToggle = MutableStateFlow<Boolean>(false)
    val isLoadingToggle = _isLoadingToggle.asStateFlow()

    fun fetchFavouritesFromDraftOrder() {
        viewModelScope.launch {
            repository.fetchAllDraftOrders().collect { draftOrderResponse ->
                _draftOrderItems.value = DataState.Success(draftOrderResponse)

                UserSession.email?.let { email ->
                    filterDraftOrdersByCustomerEmailAndFavouriteNote(draftOrderResponse, email)
                }
            }
        }
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
    fun createDraftOrder(
        lineItemList: MutableList<LineItem>,
        isFavorite: Boolean,
        productID: Long,
    ) {
        _isLoadingToggle.value = true
        val note = if (isFavorite) "Favourite" else "Shopping Cart"
        fetchFavouritesFromDraftOrder()

        try {
            val draftOrders = _favourites.value

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
                        fetchFavouritesFromDraftOrder()

                        _isLoadingToggle.value = false

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
                        fetchFavouritesFromDraftOrder()
                        _isLoadingToggle.value = false

                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CreateDraftOrder", "Error creating draft order: ${e.message}")
        }
    }
    private fun filterDraftOrdersByCustomerEmailAndFavouriteNote(
        draftOrderResponse: DraftOrderResponse,
        email: String,
        favouriteNote: String = "Favourite"
    ) {
        val filteredByEmail = draftOrderResponse.draft_orders.filter { it.email == email }
        val filteredByFavourite = filteredByEmail.filter {
            it.note?.contains(favouriteNote, ignoreCase = true) == true
        }
        _favourites.value = filteredByFavourite
    }

    fun removeFavoriteDraftOrderLineItem(productId: Long, variantId: Long) {
        _isLoadingToggle.value = true
        Log.d("FavouritesViewModel", "Loading = ${isLoadingToggle.value}")
        val favoriteDraftOrder = _favourites.value.find { draftOrder ->
            draftOrder.note == "Favourite" && draftOrder.line_items.any { lineItem ->
                lineItem.product_id == productId && lineItem.variant_id == variantId.toString()
            }
        }

        favoriteDraftOrder?.let { draftOrder ->
            val existingLineItems = draftOrder.line_items
            val mutableLineItems = existingLineItems.toMutableList()

            mutableLineItems.removeAll { lineItem ->
                lineItem.product_id == productId && lineItem.variant_id == variantId.toString()
            }

            if (mutableLineItems.size != existingLineItems.size) {
                if (mutableLineItems.isEmpty()) {

                    viewModelScope.launch {
                        draftOrder.id?.let { id ->
                            repository.removeDraftOrder(id)
                            _isLoadingToggle.value = false

                            Log.d("FavouritesViewModel", "Deleted favorite draft order with ID: $id")

                            _favourites.value = _favourites.value.filterNot { it.id == id }
                            Log.d("FavouritesViewModel", "Updated favourite draft orders: ${_favourites.value}")
                        }
                    }
                } else {
                    val updatedDraftOrder = draftOrder.copy(line_items = mutableLineItems)

                    val shoppingCartDraftOrder = DraftOrderRequest(
                        draft_order = updatedDraftOrder
                    )

                    viewModelScope.launch {
                        draftOrder.id?.let { id ->
                            repository.updateDraftOrder(id, shoppingCartDraftOrder)
                            Log.d("FavouritesViewModel", "Updated favorite draft order with ID: $id")
                            fetchFavouritesFromDraftOrder()
                            _isLoadingToggle.value = false

                            Log.d("FavouritesViewModel", "Updated favourite draft orders: ${_favourites.value}")
                        }
                    }
                }
            } else {
                _isLoadingToggle.value = false
                Log.d("FavouritesViewModel", "No matching line items found for removal.")
            }
        } ?: Log.d("FavouritesViewModel", "No matching favorite draft order found.")
    }

    fun checkIfItemIsInFavouriteDraftOrder(productId: Long, variantId: Long): Boolean {
        val favoriteDraftOrder = _favourites.value.find { it.note == "Favourite" }
        return favoriteDraftOrder?.line_items?.any {
            it.product_id == productId && it.variant_id == variantId.toString()
        } ?: false
    }
}

