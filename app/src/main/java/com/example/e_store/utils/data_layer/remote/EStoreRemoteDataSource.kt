package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.CustomCollection

interface EStoreRemoteDataSource {
    suspend fun fetchBrands(): Flow<List<Brand>>
    suspend fun fetchForUProducts(): Flow<List<Product>>
    suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse>?>
    suspend fun fetchCustomCollections(): Flow<List<CustomCollection>>
    suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>>


}