package com.example.e_store.utils.data_layer

import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSource
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSourceImplTest
import com.example.e_store.utils.test_utils.BrandsMockModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/// subjectUnderTest_actionOrInput_resultState

///Set1
//    suspend fun fetchBrands(): Flow<List<Brand>>
//    suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {}

///Set2

///Set3
class EStoreRepositoryImplTest {

    lateinit var remoteDataSource: EStoreRemoteDataSource
    lateinit var repository: EStoreRepository

    @Before
    fun createRepository() {
        remoteDataSource = EStoreRemoteDataSourceImplTest()
        repository = EStoreRepositoryImpl.getInstance(remoteDataSource)
    }

    //fetchBrands()
    @Test
    fun fetchBrands_withValidResponse_returnsCorrectBrandList() = runTest {
        // When
        val result = repository.fetchBrands().first()

        // Then Assert
        assertEquals(4, result.size)
        assertEquals("brand-1", result[0].handle)
        assertEquals("Brand 1", result[0].title)
    }

    @Test
    fun fetchBrands_withSingleBrand_returnsSingleItemList() = runTest {
        // Given: Set list with a single brand
        BrandsMockModel.brands = listOf(BrandsMockModel.brand1)

        // When
        val result = repository.fetchBrands().first()

        // Then Assert
        assertEquals(1, result.size)
        assertEquals(BrandsMockModel.brand1.handle, result[0].handle)
    }

    @Test
    fun fetchBrands_withZeroBrands_returnsEmptyList() = runTest {
        // Given: Set list with a single brand
        BrandsMockModel.brands = emptyList()

        // When
        val result = repository.fetchBrands().first()

        // Then Assert
        assertEquals(0, result.size)
    }

    @Test
    fun fetchBrands_withDuplicateTitle_returnsFilteredBrandList() = runTest {
        // Given: Set brands with duplicate IDs
        BrandsMockModel.brands = listOf(BrandsMockModel.brand4, BrandsMockModel.brand4)

        // When
        val result = repository.fetchBrands().first()

        // Then Assert
        assertEquals(1, result.size)
        assertEquals(BrandsMockModel.brand4.id, result[0].id)
    }





}