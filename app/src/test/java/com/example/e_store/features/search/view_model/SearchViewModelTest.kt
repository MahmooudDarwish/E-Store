package com.example.e_store.features.search.view_model

import com.example.e_store.utils.data_layer.FakeEStoreRepositoryEmp
import com.example.e_store.utils.shared_models.DataState
import org.junit.Before
import org.junit.Test


class SearchViewModelTest {


    lateinit var fakeRepository: FakeEStoreRepositoryEmp
    lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeEStoreRepositoryEmp()
        viewModel = SearchViewModel(fakeRepository)
    }

    @Test
    fun fetchAllProducts_setsProductsStateToSuccess() {
        viewModel.fetchAllProducts()
        val productsState = viewModel.products.value
        assert(productsState is DataState.Success)
    }

}