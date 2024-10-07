package com.example.e_store.utils.data_layer.remote

import Order
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.ProductResponse
import com.example.e_store.utils.shared_models.SingleProductResponse

interface EStoreRemoteDataSource {
    suspend fun fetchBrands(): Flow<List<Brand>>
    suspend fun fetchForUProducts(): Flow<List<Product>>
    suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse>?>
    suspend fun fetchCustomCollections(): Flow<List<CustomCollection>>
    suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>>
    suspend fun fetchCategoriesProducts(): Flow<List<Product>>

    //fetch all products
    suspend fun fetchProducts(): Flow<List<Product>>

    suspend fun fetchProduct(productId: Long): SingleProductResponse



    //shop cart draft order
    suspend fun createDraftOrder(shoppingCartDraftOrder: DraftOrderRequest)

    suspend fun updateDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrder: DraftOrderRequest
    )

    suspend fun fetchDraftOrderByID(draftOrderId: Long): DraftOrderResponse

    suspend fun removeDraftOrder(draftOrderId: Long)

    suspend fun fetchAllDraftOrders(
    ): Flow<DraftOrderResponse>

    suspend fun fetchAllOrders(email: String): Flow<List<Order>>

    suspend fun updateDraftOrderToCompleteDraftOrder(draftOrderId: Long)

}