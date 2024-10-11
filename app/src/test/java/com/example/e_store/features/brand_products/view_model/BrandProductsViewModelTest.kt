package com.example.e_store.features.brand_products.view_model

import com.example.e_store.utils.data_layer.FakeEStoreRepositoryEmp
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.test_utils.BrandsMockModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class BrandProductsViewModelTest{
    lateinit var repository : FakeEStoreRepositoryEmp
    lateinit var viewModel: BrandProductsViewModel

    @Before
    fun createRepository() {
        repository = FakeEStoreRepositoryEmp()
        viewModel = BrandProductsViewModel(repository)
    }


    @Test
    fun fetchBrands_returnsAllBrands() = runTest {
        // Given brands contains the expected data
        val expectedBrands = BrandsMockModel.brands

        //When fetching brands
        val result = repository.fetchBrands()

        //Then the result contains all brands
        assertEquals(expectedBrands, result.first())
    }

    @Test
    fun fetchBrands_returnsNonEmptyList() = runTest {
        //When fetching brands
        val result = repository.fetchBrands()

        //Then the result is not empty
        assert(result.first().isNotEmpty())
    }

    @Test
    fun fetchBrands_returnsExpectedNumberOfBrands() = runTest {
        //Given you have a way to determine the expected size
        val expectedSize = BrandsMockModel.brands.size

        //When fetching brands
        val result = repository.fetchBrands()

        // Then Check if the size of the emitted list matches the expected size
        assertEquals(expectedSize, result.first().size)
    }

    @Test
    fun fetchBrands_returnsEmptyList_whenNoBrandsAvailable() = runTest {
        // Given brands is empty
        BrandsMockModel.brands = emptyList()

        //When fetching brands
        val result = repository.fetchBrands()

        // Check that the result is an empty list
        assertEquals(emptyList<Brand>(), result.first())
    }

}