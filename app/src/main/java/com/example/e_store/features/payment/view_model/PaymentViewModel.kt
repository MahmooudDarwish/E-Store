package com.example.e_store.features.payment.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(val repository: EStoreRepository) : ViewModel() {


    fun sendEmailAndDeleteDraftOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDraftOrderToCompleteDraftOrder(DraftOrderIDHolder.draftOrderId!!)
            repository.removeDraftOrder(DraftOrderIDHolder.draftOrderId!!)
        }
    }
}


