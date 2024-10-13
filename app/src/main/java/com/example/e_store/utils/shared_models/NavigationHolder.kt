package com.example.e_store.utils.shared_models

object NavigationHolder {
    var id: Long? = null
    var address1: String? = null
    var city: String? = null
    var firstName: String? = null
    var country: String? = null
    var default: Boolean? =false
    var phone: String? = null
    var pageName: String? = null
    var country_code : String? = null

}

sealed class DeletionState {
    object CanDelete : DeletionState()
    data class CannotDelete(val message: String) : DeletionState()
}

