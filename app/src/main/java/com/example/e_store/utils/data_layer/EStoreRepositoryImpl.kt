package com.example.e_store.utils.data_layer

import android.util.Log
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSource
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.data_layer.local.room.EStoreLocalDataSource
import com.example.e_store.utils.shared_models.DraftOrderRequest

class EStoreRepositoryImpl private constructor(
    private var eStoreRemoteDataSource: EStoreRemoteDataSource,
    private var eStoreLocalDataSource: EStoreLocalDataSource,
) : EStoreRepository {

    private  val TAG = "EShopRepositoryImpl"

    companion object{
        private var instance: EStoreRepositoryImpl? = null
        fun getInstance(
            moviesRemoteDataSource: EStoreRemoteDataSource,
            moviesLocalDataSource: EStoreLocalDataSource
        ): EStoreRepositoryImpl {
            return instance ?: synchronized(this){
                val temp = EStoreRepositoryImpl(
                    moviesRemoteDataSource, moviesLocalDataSource)
                instance = temp
                temp
            }

        }
    }

    override suspend fun fetchBrands(): Flow<List<Brand>> {
        return eStoreRemoteDataSource.fetchBrands()
    }
    override suspend fun fetchCustomCollections(): Flow<List<CustomCollection>> {
        return eStoreRemoteDataSource.fetchCustomCollections()
    }

    override suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchBrandProducts(brandId)
    }

    override suspend fun fetchCategoriesProducts(): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchCategoriesProducts()
    }

    override suspend fun fetchForUProducts(): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchForUProducts()
    }
    override suspend fun fetchProducts(): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchProducts()
    }

    override suspend fun fetchDiscountCodes( ): Flow<List<DiscountCodesResponse>?> {
        return eStoreRemoteDataSource.fetchDiscountCodes()
    }

    override suspend fun createDraftOrder(shoppingCartDraftOrder: DraftOrderRequest) {
        Log.d(TAG, "createShoppingCartDraftOrder: $shoppingCartDraftOrder")
        eStoreRemoteDataSource.createDraftOrder(shoppingCartDraftOrder)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrder: DraftOrderRequest
    ) {
        eStoreRemoteDataSource.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
    }

    override suspend fun fetchDraftOrderByID(draftOrderId: Long): DraftOrderResponse {
        return eStoreRemoteDataSource.fetchDraftOrderByID(draftOrderId)
    }

    override suspend fun removeDraftOrder(draftOrderId: Long) {
        eStoreRemoteDataSource.removeDraftOrder(draftOrderId)
    }

    override suspend fun fetchAllDraftOrders(): Flow<DraftOrderResponse> {
        return eStoreRemoteDataSource.fetchAllDraftOrders()
    }


}