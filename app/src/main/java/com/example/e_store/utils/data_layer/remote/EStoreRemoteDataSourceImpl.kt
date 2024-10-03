package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.data_layer.remote.shopify.ShopifyAPIServices
import com.example.e_store.utils.data_layer.remote.shopify.ShopifyRetrofitHelper
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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

    override suspend fun fetchBrands(): Flow<List<Brand>> {
        val response = apiService.fetchBrands().smart_collections
        return flowOf(response)
    }

    override suspend fun fetchForUProducts(): Flow<List<Product>>  {
        val response = apiService.fetchProducts(limit = 4).products
        return flowOf(response)

    }

}