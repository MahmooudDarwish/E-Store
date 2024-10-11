package com.example.e_store.features.favourites.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(private val repository: EStoreRepository) : ViewModel() {

    private val _draftOrderItems =
        MutableStateFlow<DataState<DraftOrderResponse>>(DataState.Loading)
    val draftOrderItems = _draftOrderItems.asStateFlow()

    private val _favourites = MutableStateFlow<List<DraftOrderDetails>>(emptyList())
    val favourites = _favourites.asStateFlow()

    fun fetchFavouritesFromDraftOrder() {
        viewModelScope.launch {
            repository.fetchAllDraftOrders().collect { draftOrderResponse ->
                _draftOrderItems.value = DataState.Success(draftOrderResponse)
                Log.d("FavouritesInfoViewModel", "Draft Orders: $draftOrderResponse")

                UserSession.email?.let {
                    filterDraftOrdersByCustomerEmailAndFavouriteNote(draftOrderResponse, it)
                }
            }
        }
    }

    fun filterDraftOrdersByCustomerEmailAndFavouriteNote(
        draftOrderResponse: DraftOrderResponse,
        email: String,
        favouriteNote: String = "Favourite"
    ) {
        val filteredByEmail = draftOrderResponse.draft_orders.filter { it.email == email }
        Log.d("FavouritesInfoViewModel", "Filtered Draft Orders for $email: $filteredByEmail")

        val filteredByFavourite = filteredByEmail.filter {
            it.note?.contains(favouriteNote, ignoreCase = true) == true
        }
        Log.d("FavouritesInfoViewModel", "Filtered Draft Orders for $favouriteNote: $filteredByFavourite")

        _favourites.value = filteredByFavourite
    }

    fun removeFavoriteDraftOrderLineItem(productId: Long, variantId: Long) {
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

                            _favourites.value = _favourites.value.toMutableList().apply {
                                removeIf { it.id == id }
                                add(updatedDraftOrder)
                            }
                            Log.d("FavouritesViewModel", "Updated favourite draft orders: ${_favourites.value}")
                        }
                    }
                }
            } else {
                Log.d("FavouritesViewModel", "No matching line items found for removal.")
            }
        } ?: Log.d("FavouritesViewModel", "No matching favorite draft order found.")
    }

}

