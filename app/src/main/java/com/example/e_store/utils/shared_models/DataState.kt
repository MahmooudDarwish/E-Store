package com.example.e_store.utils.shared_models


sealed class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val message: Int) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}
