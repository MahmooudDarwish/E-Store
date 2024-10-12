package com.example.e_store.utils.shared_models

object NavigationHolder {
    var id: Long? = null
    var pageName: String? = null

    fun reset() {
        id = null
        pageName = null
    }
}

sealed class DeletionState {
    object CanDelete : DeletionState()
    data class CannotDelete(val message: String) : DeletionState()
}