package com.example.e_store.features.search.view_model

import com.example.e_store.utils.data_layer.FakeEStoreRepositoryEmp
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.test_utils.ProductMockModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class SearchViewModelTest {
    lateinit var fakeRepository: FakeEStoreRepositoryEmp
    lateinit var viewModel: SearchViewModel
    lateinit var productTest: List<Product>



    //given
    @Before
    fun setUp() {
        fakeRepository = FakeEStoreRepositoryEmp()
        viewModel = SearchViewModel(fakeRepository)
        productTest = ProductMockModel.products
    }

    @Test
    fun fetchAllProducts_setsProductsStateToSuccess() = runTest {
        // When the ViewModel fetches all products
        viewModel.fetchAllProducts()

        // Allow coroutine to finish
        advanceUntilIdle()

        // Then the products state is set to Success
        val productsState = viewModel.products.value
        assertTrue(productsState is DataState.Success)

        // And the products match the expected test data
        val products = (productsState as DataState.Success).data
        assertEquals(productTest, products)
    }

    @Test
    fun fetchAllProducts_setsProductsStateToLoading() = runTest {
        // When the ViewModel fetches all products
        viewModel.fetchAllProducts()

        // Then the products state is set to Loading
        val productsState = viewModel.products.value
        assertTrue(productsState is DataState.Loading)
    }

    @Test
    fun fetchAllProducts_setsProductsStateToError() = runTest {
        // When the fake repository throws an error
        fakeRepository.shouldThrowError = true
        viewModel.fetchAllProducts()

        // Allow coroutine to finish
        advanceUntilIdle()

        // Then the products state is set to Error
        val productsState = viewModel.products.value
        assertTrue(productsState is DataState.Error)

        // And the error message matches the expected string resource ID
        assertEquals(2131820840, (productsState as DataState.Error).message)
    }

    @Test
    fun fetchAllProducts_setsProductsStateToEmptySuccess() = runTest {
        // When the repository returns an empty list of products
        fakeRepository.listIsEmpty = true
        viewModel.fetchAllProducts()

        // Allow coroutine to finish
        advanceUntilIdle()

        // Then the products state is set to Success with an empty list
        val productsState = viewModel.products.value
        val products = (productsState as DataState.Success).data
        assertTrue(products.isEmpty())
    }

    @Test
    fun fetchAllProducts_withLargeDataSet_handlesSuccessfully() = runTest {
        // When the repository returns a large list of products
        val largeProductList = List(1000) { ProductMockModel.product1 }  // Simulate 1000 products
        fakeRepository.expectedProducts = largeProductList
        viewModel.fetchAllProducts()

        // Allow coroutine to finish
        advanceUntilIdle()

        // Then the products state is Success and contains all products
        val productsState = viewModel.products.value
        assertTrue(productsState is DataState.Success)
        val products = (productsState as DataState.Success).data
        assertEquals(1000, products.size)
    }


}