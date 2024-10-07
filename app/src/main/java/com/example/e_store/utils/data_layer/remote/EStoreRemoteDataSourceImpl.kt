package com.example.e_store.utils.data_layer.remote

import Order
import com.example.e_store.utils.data_layer.remote.shopify.ShopifyAPIServices
import com.example.e_store.utils.data_layer.remote.shopify.ShopifyRetrofitHelper
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import android.util.Log
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import kotlinx.coroutines.flow.flow
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.ProductResponse
import com.example.e_store.utils.shared_models.SingleProductResponse

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

    override suspend fun fetchProducts(): Flow<List<Product>> {
        val response = apiService.fetchProducts().products
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
                val response =
                    apiService.fetchProducts(limit = 4, collectionId = it.id.toString()).products
                flowOf(response)
            } ?: flowOf(emptyList())

        } catch (e: Exception) {
            Log.d(TAG, "customCollections error: ${e.message}")
            flowOf(emptyList())
        }
    }

    override suspend fun fetchCategoriesProducts(): Flow<List<Product>> {
        return try {
            val customCollections = fetchCustomCollections()
            val allProducts = mutableListOf<Product>()
            val productIdsSet = mutableSetOf<Long>()

            customCollections.collect { response ->
                val filteredCollections = response.filter { collection ->
                    !collection.title.equals(APIKeys.HOME, ignoreCase = true)
                }

                for (collection in filteredCollections) {
                    val products =
                        apiService.fetchProducts(collectionId = collection.id.toString()).products

                    val uniqueProducts = products.filter { product ->
                        product.id !in productIdsSet
                    }

                    uniqueProducts.forEach { product ->
                        productIdsSet.add(product.id)
                        allProducts.add(product)
                    }
                }
            }

            flowOf(allProducts)

        } catch (e: Exception) {
            Log.d(TAG, "customCollections error: ${e.message}")
            flowOf(emptyList())
        }
    }


    override suspend fun fetchCustomCollections(): Flow<List<CustomCollection>> {
        val response = apiService.fetchCustomCollections().customCollections
        return flowOf(response)
    }

    override suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {
        val response = apiService.fetchProducts(collectionId = brandId).products
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
                    Log.e(
                        TAG,
                        "Failed to fetch pricing rules: ${priceRuleResponse.errorBody()?.string()}"
                    )
                    emit(emptyList()) // Emit an empty list in case of failure
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching discount codes: ${e.message}")
                emit(emptyList()) // Emit an empty list in case of exception
            }
        }
    }


    override suspend fun createDraftOrder(shoppingCartDraftOrder: DraftOrderRequest) {
        Log.d(TAG, "createShoppingCartDraftOrder: $shoppingCartDraftOrder")
        apiService.createDraftOrder(shoppingCartDraftOrder)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrder: DraftOrderRequest
    ) {
        apiService.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
    }

    override suspend fun removeDraftOrder(draftOrderId: Long) {
        apiService.removeDraftDraftOrder(draftOrderId)
    }

    override suspend fun fetchDraftOrderByID(draftOrderId: Long): DraftOrderResponse {
        return apiService.fetchDraftOrderByID(draftOrderId)
    }

    override suspend fun fetchAllDraftOrders(): Flow<DraftOrderResponse> {
        return flow {
            val response = apiService.fetchAllDraftOrders()
            emit(response)
        }
    }
    override suspend fun fetchProduct(productId: Long): SingleProductResponse {
        return apiService.fetchProduct(productId)
    }

    override suspend fun fetchAllOrders(email: String): Flow<List<Order>> {

        val response = apiService.fetchOrders().orders

        val filteredOrders = response.filter {
            it.email == email
        }
        return flowOf(filteredOrders)
    }

    override suspend fun updateDraftOrderToCompleteDraftOrder(draftOrderId: Long) {
        apiService.updateDraftOrderToCompleteDraftOrder(draftOrderId = draftOrderId)
    }


}