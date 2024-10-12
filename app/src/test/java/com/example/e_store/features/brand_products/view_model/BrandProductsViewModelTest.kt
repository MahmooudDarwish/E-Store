package com.example.e_store.features.brand_products.view_model

import com.example.e_store.utils.data_layer.FakeEStoreRepositoryEmp
import com.example.e_store.utils.test_utils.ProductMockModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class BrandProductsViewModelTest{
    lateinit var fakeRepository : FakeEStoreRepositoryEmp
    lateinit var viewModel: BrandProductsViewModel

    @Before
    fun createRepository() {
        fakeRepository = FakeEStoreRepositoryEmp()
        viewModel = BrandProductsViewModel(fakeRepository)
    }

    @Test
    fun testFetchBrandProducts_existingBrandId_returnsExpectedProductList() = runTest {
        // Given
        val brandId = "1"
        val expectedProducts = listOf(ProductMockModel.product1, ProductMockModel.product3)

        // When
        val result = fakeRepository.fetchBrandProducts(brandId)

        // Then
        assertEquals(expectedProducts, result.first())
    }

    @Test
    fun testFetchBrandProducts_nonExistingBrandId_returnsEmptyList() = runTest {
        // Given
        val brandId = "non-existent-brand-id"

        // When
        val result = fakeRepository.fetchBrandProducts(brandId)

        // Then
        assertTrue(result.first().isEmpty())
    }

    @Test
    fun testFetchBrandProducts_emptyBrandId_returnsEmptyList() = runTest {
        // Given
        val brandId = ""

        // When
        val result = fakeRepository.fetchBrandProducts(brandId)

        // Then
        assertTrue(result.first().isEmpty())
    }


    @Test(expected = NullPointerException::class)
    fun testFetchBrandProducts_nullBrandId_throwsIllegalArgumentException() = runTest {
        // Given
        val brandId: String? = null

        // When
        fakeRepository.fetchBrandProducts(brandId!!)

        //Then throw error
    }






}