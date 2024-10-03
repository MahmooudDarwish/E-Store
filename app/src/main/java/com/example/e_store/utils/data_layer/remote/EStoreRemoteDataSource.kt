package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.Product
import kotlinx.coroutines.flow.Flow

interface EStoreRemoteDataSource {
     suspend fun fetchBrands(): Flow<List<Brand>>
     suspend fun fetchForUProducts(): Flow<List<Product>>

}