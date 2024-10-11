package com.example.e_store.features.home.view_model

import com.example.e_store.features.brand_products.view_model.BrandProductsViewModel
import com.example.e_store.utils.data_layer.FakeEStoreRepositoryEmp
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.test_utils.BrandsMockModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class HomeViewModelTest{
    lateinit var fakeRepository : FakeEStoreRepositoryEmp
    lateinit var viewModel: BrandProductsViewModel

    @Before
    fun createRepository() {
        fakeRepository = FakeEStoreRepositoryEmp()
        viewModel = BrandProductsViewModel(fakeRepository)
    }


    @Test
    fun fetchBrands_returnsAllBrands() = runTest {
        // Given brands contains the expected data
        val expectedBrands = BrandsMockModel.brands

        //When fetching brands
        val result = fakeRepository.fetchBrands()

        //Then the result contains all brands
        Assert.assertEquals(expectedBrands, result.first())
    }

    @Test
    fun fetchBrands_returnsNonEmptyList() = runTest {
        //When fetching brands
        val result = fakeRepository.fetchBrands()

        //Then the result is not empty
        assert(result.first().isNotEmpty())
    }

    @Test
    fun fetchBrands_returnsExpectedNumberOfBrands() = runTest {
        //Given you have a way to determine the expected size
        val expectedSize = BrandsMockModel.brands.size

        //When fetching brands
        val result = fakeRepository.fetchBrands()

        // Then Check if the size of the emitted list matches the expected size
        Assert.assertEquals(expectedSize, result.first().size)
    }

    @Test
    fun fetchBrands_returnsEmptyList_whenNoBrandsAvailable() = runTest {
        // Given brands is empty
        BrandsMockModel.brands = emptyList()

        //When fetching brands
        val result = fakeRepository.fetchBrands()

        // Check that the result is an empty list
        Assert.assertEquals(emptyList<Brand>(), result.first())
    }

}