package com.example.e_store.features.checkout.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.R
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AppliedDiscount
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(val repo: EStoreRepository) : ViewModel() {


    private val TAG = "CheckoutViewModel"


    private val _draftOrder =
        MutableStateFlow<DataState<DraftOrderDetails?>>(DataState.Loading)
    val draftOrder = _draftOrder.asStateFlow()

    private val _discountCodes =
        MutableStateFlow<DataState<AppliedDiscount>>(DataState.Loading)
    val discountCodes = _discountCodes.asStateFlow()


    private val _defaultAddress = MutableStateFlow<DataState<Address?>>(DataState.Loading)

    val defaultAddress = _defaultAddress.asStateFlow()


    fun fetchDefaultAddress() {
        viewModelScope.launch()
        {
            try {
                repo.fetchDefaultAddress(
                    UserSession.shopifyCustomerID!!
                ).collect { addressResponse ->
                    Log.d("CheckotextutScreen", "Coupon Code: ${addressResponse}")
                    if (addressResponse != null) {
                        _defaultAddress.value = DataState.Success(addressResponse)
                        Log.d(
                            "CheckotextutScreen",
                            "Coupon Code: ${DataState.Success(addressResponse)}"
                        )

                    } else {
                        _defaultAddress.value = DataState.Success(null)
                    }
                }

            } catch (ex: Exception) {
                _defaultAddress.value = DataState.Error(R.string.unexpected_error)
                Log.e(TAG, "Error fetching shopping cart items", ex)
            }


        }
    }

    fun addDeliveryAddress(address: Address) {
        val currentState = _draftOrder.value

        // Check if the current state is Success
        if (currentState is DataState.Success) {
            val draftOrderDetails = currentState.data

            // Set the addresses
            if (draftOrderDetails != null) {
                draftOrderDetails.shipping_address = address
            }
            if (draftOrderDetails != null) {
                draftOrderDetails.billing_address = address
            }

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    if (draftOrderDetails != null) {
                        draftOrderDetails.id?.let {
                            draftOrderDetails?.let { it1 -> DraftOrderRequest(it1) }?.let { it2 ->
                                repo.updateDraftOrder(it,
                                    it2
                                )
                            }
                        }
                    }
                    fetchDefaultAddress() // Fetch updated addresses
                } catch (ex: Exception) {
                    // Handle the exception (log it, show a message to the user, etc.)
                    Log.e("CheckoutViewModel", "Error updating draft order: ${ex.message}")
                }
            }
        } else if (currentState is DataState.Loading) {
            // Handle loading state if necessary, maybe show a loading spinner
            Log.d("CheckoutViewModel", "Draft order is loading, please wait...")
        } else if (currentState is DataState.Error) {
            // Handle error state (show error message)
            Log.e("CheckoutViewModel", "Error fetching draft order: ${currentState.message}")
        }
    }

    fun fetchDraftOrderByID(draftOrderId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.fetchDraftOrderByID(draftOrderId).collect { draftOrderResponse ->
                    Log.d("CheckotextutScreen", "Coupon Code: ${draftOrderResponse}")
                    if (draftOrderResponse != null) {
                        _draftOrder.value = DataState.Success(draftOrderResponse)
                        Log.d(
                            "CheckotextutScreen",
                            "Coupon Code: ${DataState.Success(draftOrderResponse)}"
                        )

                    } else {
                        _draftOrder.value = DataState.Success(null)
                    }
                }
            } catch (ex: Exception) {
                _draftOrder.value = DataState.Error(R.string.unexpected_error)
                Log.e(TAG, "Error fetching shopping cart items", ex)
            }
        }
    }


    fun updateDraftOrder(draftOrderId: Long, discount: AppliedDiscount) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Collect the current state of the draft order
                _draftOrder.collect { dataState ->
                    if (dataState is DataState.Success) {
                        val draftOrderDetails = dataState.data

                        // Make sure draftOrderDetails is not null
                        draftOrderDetails?.let {
                            // Perform the update logic with the draft order details and discount
                            Log.d(
                                TAG,
                                "Updating draft order: $draftOrderDetails with discount: $discount"
                            )

                            // Update draftOrderDetails with new discount (adjust this to your update logic)
                            draftOrderDetails.applied_discount = discount
                            val draftOrderRequest = DraftOrderRequest(draftOrderDetails)
                            repo.updateDraftOrder(draftOrderId, draftOrderRequest)
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Error updating draft order: ${ex.message}")
            }
        }
    }


    fun sendEmailAnddeleteDraftOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateDraftOrderToCompleteDraftOrder(DraftOrderIDHolder.draftOrderId!!)
            repo.removeDraftOrder(DraftOrderIDHolder.draftOrderId!!)
        }
    }
/*

    fun applyDiscount(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.fetchDiscountCodesByCode(code).collect { discountCodesResponse ->
                    if (discountCodesResponse != null) {
                        Log.d(TAG, "Coupon Code: ${discountCodesResponse}")
                        _discountCodes.value = DataState.Success(discountCodesResponse)
                        if (DraftOrderIDHolder.draftOrderId != null && discountCodesResponse != null) {
                            Log.d(
                                TAG,
                                "Coupon Code: ${DataState.Success(discountCodesResponse)}"
                            )


                            DraftOrderIDHolder.draftOrderId?.let {
                                updateDraftOrder(
                                    it,
                                    discountCodesResponse
                                )
                            }
                        } else {
                            Log.d(
                                TAG,
                                "Error updating draft order: $DraftOrderIDHolder.draftOrderId"
                            )
                        }
                    } else {
                    }
                }

            } catch (ex: Exception) {
                _discountCodes.value = DataState.Error(R.string.unexpected_error)
                Log.e(TAG, "Error fetching shopping cart items", ex)
            } finally {
                fetchDraftOrderByID(DraftOrderIDHolder.draftOrderId!!)
                Log.d("CheckotextutScreen", "Coupon Code: ${DraftOrderIDHolder.draftOrderId}")

            }
        }

    }
*/



    fun applyDiscount(code: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.fetchDiscountCodesByCode(code).collect { discountCodesResponse ->
                    if (discountCodesResponse != null ) {
                        Log.d(TAG, "Coupon Code: $discountCodesResponse")
                        _discountCodes.value = DataState.Success(discountCodesResponse)

                        // Check if draftOrderId is available and update the order
                        DraftOrderIDHolder.draftOrderId?.let {
                            updateDraftOrder(it, discountCodesResponse)
                        } ?: run {
                            Log.d(TAG, "Error updating draft order: $DraftOrderIDHolder.draftOrderId")
                        }
                        delay(500)
                        // Notify success only if we have processed the response successfully
                        onComplete(true)
                    } else {
                        Log.d(TAG, "No discount code found.")
                        onComplete(false) // Notify failure if discount code is null
                    }
                }
            } catch (ex: Exception) {
                _discountCodes.value = DataState.Error(R.string.unexpected_error)
                Log.e(TAG, "Error fetching discount codes", ex)
                onComplete(false) // Notify failure on exception
            } finally {
                // Removed onComplete(true) from here
                if (DraftOrderIDHolder.draftOrderId != null && _discountCodes.value is DataState.Success) {
                    fetchDraftOrderByID(DraftOrderIDHolder.draftOrderId!!)
                    Log.d("CheckoutScreen", "Coupon Code: ${DraftOrderIDHolder.draftOrderId}")
                }
            }
        }
    }


}


