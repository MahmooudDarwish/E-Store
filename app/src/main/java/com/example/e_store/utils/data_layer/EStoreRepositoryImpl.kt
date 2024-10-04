package com.example.e_store.utils.data_layer

import com.example.e_store.utils.data_layer.local.room.EStoreLocalDataSource
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSource
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.PriceRule
import com.example.e_store.utils.shared_models.Product
import android.util.Log

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


    override suspend fun fetchForUProducts(): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchForUProducts()
    }
    override suspend fun fetchDiscountCodes( ): Flow<List<DiscountCodesResponse>?> {
        return eStoreRemoteDataSource.fetchDiscountCodes()
    }
}