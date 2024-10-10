package com.example.e_store.features.authentication.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_store.utils.data_layer.EStoreRepository
import com.example.e_store.utils.data_layer.local.shared_pref.CustomerSharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModelFactory(private val auth: FirebaseAuth, private val repository: EStoreRepository, private val customerSharedPreferences: CustomerSharedPreferencesHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            return AuthenticationViewModel(auth,repository,customerSharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
