package com.example.e_store.utils.data_layer.remote

import android.util.Log
import com.example.e_store.utils.data_layer.remote.shopify.ShopifyAPIServices
import com.example.e_store.utils.data_layer.remote.shopify.ShopifyRetrofitHelper
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EStoreRemoteDataSourceImpl private constructor() : EStoreRemoteDataSource {

    private val apiService: ShopifyAPIServices = ShopifyRetrofitHelper.api

    private val TAG = "EShopRemoteDataSourceImpl"
    companion object {
        @Volatile
        private var instance: EStoreRemoteDataSourceImpl? = null

        fun getInstance(): EStoreRemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                instance ?: EStoreRemoteDataSourceImpl().also { instance = it }
            }
        }
    }
     override suspend  fun getBrands(): Flow<List<Brand>?> = flow {
        try {


            val response = apiService.getBrands()
            if (response.isSuccessful) {
                Log.d(TAG, "getBrands: ${response}")
                emit(response.body()!!.smart_collections)
            } else {

                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error $errorBody")

                emit(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error ${e.toString()}")

            emit(null)
        }
    }

}