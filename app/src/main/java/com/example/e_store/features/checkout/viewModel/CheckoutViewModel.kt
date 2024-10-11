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


    fun sendEmailAndDeleteDraftOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateDraftOrderToCompleteDraftOrder(DraftOrderIDHolder.draftOrderId!!)
            repo.removeDraftOrder(DraftOrderIDHolder.draftOrderId!!)
        }
    }

    fun applyDiscount(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.fetchDiscountCodesByCode(code).collect { discountCodesResponse ->
                    if (discountCodesResponse != null) {
                        _discountCodes.value = DataState.Success(discountCodesResponse)



                        if (DraftOrderIDHolder.draftOrderId != null && discountCodesResponse != null) {


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

}


