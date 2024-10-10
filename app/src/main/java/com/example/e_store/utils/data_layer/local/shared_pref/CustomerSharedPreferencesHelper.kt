package com.example.e_store.utils.data_layer.local.shared_pref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.e_store.utils.shared_models.Customer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CustomerSharedPreferencesHelper ( context: Context){
    private val sharedPreferences = context.getSharedPreferences("CustomerPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveCustomer(customer: Customer) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(customer)
        editor.putString("customer", json)
        Log.d("CustomerSharedPreferencesHelper", "Customer saved: $customer")
        editor.apply()
    }

    fun getCustomer(): Customer? {
        val json = sharedPreferences.getString("customer", null)
        val type = object : TypeToken<Customer>() {}.type
        return Gson().fromJson(json, type)

    }

    fun clearCustomer() {
        val editor = sharedPreferences.edit()
        editor.remove("customer")
        editor.apply()
    }
}