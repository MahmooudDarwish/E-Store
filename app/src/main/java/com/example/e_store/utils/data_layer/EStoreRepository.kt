package com.example.e_store.utils.data_layer

import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow

interface EStoreRepository {
    suspend fun getBrands(): Flow<List<Brand>?>

}