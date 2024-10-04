package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.data_layer.remote.shopify.ShopifyAPIServices
import com.example.e_store.utils.data_layer.remote.shopify.ShopifyRetrofitHelper
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import android.util.Log
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import kotlinx.coroutines.flow.flow
import com.example.e_store.utils.shared_models.Product
import retrofit2.Response
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.constants.APIKeys

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

    override suspend fun fetchForUProducts(): Flow<List<Product>> {
        return try {
            val customCollections = fetchCustomCollections()

            var homeCollection: CustomCollection? = null

            customCollections.collect { response ->
                homeCollection = response.firstOrNull { collection ->
                    collection.title.contains(APIKeys.HOME, ignoreCase = true)
                }
            }
            homeCollection?.let {
                val response = apiService.fetchProducts(limit = 4, collectionId = it.id.toString()).products
                flowOf(response)
            } ?: flowOf(emptyList())

        } catch (e: Exception) {
            Log.d(TAG, "customCollections error: ${e.message}")
            flowOf(emptyList())
        }
    }


    override suspend fun fetchCustomCollections(): Flow<List<CustomCollection>> {
        val response = apiService.fetchCustomCollections().customCollections
        return flowOf(response)
    }


    override suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse>?> {
        return flow {
            try {
                val priceRuleResponse = apiService.fetchPricingRules()

                if (priceRuleResponse.isSuccessful) {
                    priceRuleResponse.body()?.let { body ->
                        val discountCodesList = mutableListOf<DiscountCodesResponse>()

                        for (priceRule in body.price_rules) {
                            Log.d(TAG, "priceRuleID: ${priceRule.id}")
                            discountCodesList.addAll(listOf(apiService.fetchDiscountCodes(priceRule.id)))
                        }
                        Log.d(TAG, "fetchDiscountCodes: ${discountCodesList}")
                        // Emit the accumulated discount codes
                        emit(discountCodesList)
                    } ?: run {
                        Log.e(TAG, "No price rules found.")
                        emit(emptyList())
                    }
                } else {
                    Log.e(TAG, "Failed to fetch pricing rules: ${priceRuleResponse.errorBody()?.string()}")
                    emit(emptyList()) // Emit an empty list in case of failure
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching discount codes: ${e.message}")
                emit(emptyList()) // Emit an empty list in case of exception
            }
        }
    }



}