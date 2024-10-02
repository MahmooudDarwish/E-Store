package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow

interface EStoreRemoteDataSource {
     suspend fun getBrands(): Flow<List<Brand>?>
}