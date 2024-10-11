package com.example.e_store.utils.data_layer

import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSource
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSourceImplTest
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.SingleProductResponse
import com.example.e_store.utils.test_utils.BrandsMockModel
import com.example.e_store.utils.test_utils.ProductMockModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/// subjectUnderTest_actionOrInput_resultState

///Set1
//    suspend fun fetchBrands(): Flow<List<Brand>>
//    suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {}

///Set2

///Set3
class EStoreRepositoryImplTest {

    lateinit var remoteDataSource: EStoreRemoteDataSourceImplTest
    lateinit var repository: EStoreRepository
    val expectedProducts = ProductMockModel.products

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

    //fetchBrandProducts()
    @Test
    fun fetchBrandProducts_validBrandId1_returnsExpectedProducts() = runTest {
        //When
        val result = repository.fetchBrandProducts("brand-1")

        //Then Assert
        val expected = ProductMockModel.collections["brand-1"] ?: emptyList()
        assertEquals(expected, result.first())
    }
    @Test
    fun fetchBrandProducts_validBrandId2_returnsExpectedProducts() = runTest {
        //When
        val result = repository.fetchBrandProducts("brand-2")

        //Then Assert
        val expected = ProductMockModel.collections["brand-2"] ?: emptyList()
        assertEquals(expected, result.first())
    }
    @Test
    fun fetchBrandProducts_nonExistingBrandId_returnsEmptyList() = runTest {
        //When
        val result = repository.fetchBrandProducts("non-existing-brand")

        //Then Assert
        assertEquals(emptyList<Product>(), result.first())
    }
    @Test
    fun fetchBrandProducts_emptyBrandId_returnsEmptyList() = runTest {
        //When
        val result = repository.fetchBrandProducts("")

        //Then Assert
        assertEquals(emptyList<Product>(), result.first())
    }
    @Test
    fun fetchBrandProducts_validBrandId3_returnsProductsFromFlow() = runTest {
        //When
        val result = repository.fetchBrandProducts("brand-3")

        //Then Assert
        val expected = ProductMockModel.collections["brand-3"] ?: emptyList()
        assertEquals(expected, result.first())
    }


    //fetchProducts()
    @Test
    fun fetchProducts_returnsProductsSuccessfully() = runTest {


        // Act
        val flow = repository.fetchProducts()

        // Assert - collect flow and check the emitted values
        flow.collect { products ->
            assertEquals(expectedProducts, products)
        }
    }
    @Test
    fun fetchProducts_returnsNonEmptyList() = runTest {

        // When: Call fetchProducts() from the repository
        val result = repository.fetchProducts()

        // Then: Collect flow and verify the list of products
        result.collect { products ->
            assertEquals(expectedProducts, products)
            assertEquals(expectedProducts.size, products.size)
        }
    }
    @Test
    fun fetchProducts_withValidResponseReturnsExpectedProductList() = runTest {


        // When: Call fetchProducts() from the repository
        val result = repository.fetchProducts().first()

        // Then: Assert the product list matches the expected values
        assertEquals(expectedProducts.size, result.size)
        assertEquals(expectedProducts[0].id, result[0].id)
        assertEquals(expectedProducts[0].title, result[0].title)
    }
    @Test
    fun fetchProducts_withEmptyResponseReturnsEmptyList() = runTest {
        remoteDataSource.emptyProducts = true

        // When: Call fetchProducts() from the repository
        val result = repository.fetchProducts().first()

        // Then: Assert that the result is an empty list
        assertEquals(0, result.size)
    }
    @Test
    fun fetchProducts_withMultipleSubscribers_returnsSameData() = runTest {
        // When two collectors subscribe to the products flow
        val resultFlow = repository.fetchProducts()

        val collector1 = mutableListOf<List<Product>>()
        val collector2 = mutableListOf<List<Product>>()

        // Collect data with two collectors
        launch { resultFlow.collect { collector1.add(it) } }
        launch { resultFlow.collect { collector2.add(it) } }

        advanceUntilIdle()

        // Then both collectors receive the same data
        assertEquals(collector1, collector2)
    }
    @Test
    fun fetchProducts_withUpdatesReturnsUpdatedProductList() = runTest {
        // When initial products are fetched
        val initialProducts = expectedProducts.take(2)
        remoteDataSource.expectedProducts = initialProducts
        val resultFlow = repository.fetchProducts()

        val emittedValues = mutableListOf<List<Product>>()
        launch { resultFlow.collect { emittedValues.add(it) } }

        advanceUntilIdle()

        // Update the product list
        val updatedProducts = expectedProducts
        remoteDataSource.expectedProducts = updatedProducts

        advanceUntilIdle()

        // Then the emitted values should reflect the updated product list
        assertEquals(updatedProducts, emittedValues.last())
    }


    //fetchProduct()
    @Test
    fun fetchProduct_validProductIdReturnsCorrectProduct() = runTest {
        val productId = expectedProducts.first().id

        // When the repository fetches a product by a valid product ID
        val resultFlow = repository.fetchProduct(productId)

        // Then the correct product is returned
        resultFlow.collect { response ->
            assertEquals(expectedProducts.first(), response.product)
        }
    }
    @Test
    fun fetchProduct_nonExistingProductIdReturnsEmptyResponse() = runTest {
        val nonExistingProductId = 999L

        // When the repository fetches a product by a non-existing product ID
        val resultFlow = repository.fetchProduct(nonExistingProductId)

        // Then no product is returned (empty result)
        val emittedValues = mutableListOf<SingleProductResponse>()
        resultFlow.collect { response ->
            emittedValues.add(response)
        }

        assertTrue(emittedValues.isEmpty())
    }
    @Test
    fun fetchProduct_withDelayedResponseReturnsCorrectProductAfterDelay() = runTest {
        val productId = expectedProducts[1].id
        remoteDataSource.delayTime = 3000  // Example delay in milliseconds

        // When the repository fetches a product with a delayed response
        val resultFlow = repository.fetchProduct(productId)

        // Then the correct product is returned after the delay
        resultFlow.collect { response ->
            assertEquals(expectedProducts[1], response.product)
        }
    }
    @Test
    fun fetchProduct_invalidProductIdReturnsErrorResponse() = runTest {
        val invalidProductId = -1L

        // When the repository fetches a product by an invalid product ID
        val resultFlow = repository.fetchProduct(invalidProductId)

        // Then no product is returned (empty result)
        val emittedValues = mutableListOf<SingleProductResponse>()
        resultFlow.collect { response ->
            emittedValues.add(response)
        }

        assertTrue(emittedValues.isEmpty())
    }
















}