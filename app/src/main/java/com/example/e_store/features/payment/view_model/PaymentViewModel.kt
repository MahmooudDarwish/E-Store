package com.example.e_store.features.payment.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.shared_models.DraftOrderIDHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel( val repository: EStoreRepository) : ViewModel() {

    // MutableStateFlow for each field
    private val _cardholderName = MutableStateFlow("")
    val cardholderName: StateFlow<String> = _cardholderName

    private val _cardNumber = MutableStateFlow("")
    val cardNumber: StateFlow<String> = _cardNumber

    private val _month = MutableStateFlow("")
    val month: StateFlow<String> = _month

    private val _year = MutableStateFlow("")
    val year: StateFlow<String> = _year

    private val _cvv = MutableStateFlow("")
    val cvv: StateFlow<String> = _cvv

    // Add more fields and their MutableStateFlows as needed

    // Update functions
    fun updateCardholderName(newName: String) {
        _cardholderName.value = newName
    }

    fun updateCardNumber(newNumber: String) {
        _cardNumber.value = newNumber
    }

    fun updateMonth(newMonth: String) {
        _month.value = newMonth
    }

    fun updateYear(newYear: String) {
        _year.value = newYear
    }

    fun updateCvv(newCvv: String) {
        _cvv.value = newCvv
    }


//    fun  deleteDraftOrder()
//    {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.removeDraftOrder( DraftOrderIDHolder.draftOrderId!!)
//        }
//    }

    fun sendEmailAnddeleteDraftOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDraftOrderToCompleteDraftOrder(DraftOrderIDHolder.draftOrderId!!)
            repository.removeDraftOrder(DraftOrderIDHolder.draftOrderId!!)
        }
    }
}


