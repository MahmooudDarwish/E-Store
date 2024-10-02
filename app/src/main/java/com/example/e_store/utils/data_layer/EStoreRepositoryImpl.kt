package com.example.e_store.utils.data_layer

import android.util.Log
import com.example.e_store.utils.data_layer.local.room.EStoreLocalDataSource
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSource
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow

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

    override suspend fun getBrands(): Flow<List<Brand>?> {
        return eStoreRemoteDataSource.getBrands()
    }
}