package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.shared_models.ShoppingCartDraftOrder
import com.example.e_store.utils.shared_models.ShoppingCartDraftOrderDetails
import com.example.e_store.utils.shared_models.ShoppingCartDraftOrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EStoreRemoteDataSource {
    suspend fun fetchBrands(): Flow<List<Brand>>
    suspend fun fetchForUProducts(): Flow<List<Product>>
    suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse>?>
    suspend fun fetchCustomCollections(): Flow<List<CustomCollection>>
    suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>>
    suspend fun fetchCategoriesProducts(): Flow<List<Product>>



    //shop cart draft order
    suspend fun createShoppingCartDraftOrder( shoppingCartDraftOrder: ShoppingCartDraftOrder)

    suspend fun updateShoppingCartDraftOrder(
        draftOrderId: Long,
         shoppingCartDraftOrder: ShoppingCartDraftOrder
    )

    suspend fun fetchShoppingCartDraftOrderByID( draftOrderId: Long): ShoppingCartDraftOrderResponse

    suspend fun removeShoppingCartDraftDraftOrder(draftOrderId: Long)

    suspend fun fetchAllShoppingCartDraftDraftOrdersByCustomerId(
         customerId: String
    ): Flow<List<ShoppingCartDraftOrderDetails>>


}