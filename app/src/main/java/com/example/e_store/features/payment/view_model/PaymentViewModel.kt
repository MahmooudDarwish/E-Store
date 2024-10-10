package com.example.e_store.features.payment.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel( val repository: EStoreRepository) : ViewModel() {

    fun  deleteDraftOrder()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeDraftOrder( DraftOrderIDHolder.draftOrderId!!)
        }
    }
}