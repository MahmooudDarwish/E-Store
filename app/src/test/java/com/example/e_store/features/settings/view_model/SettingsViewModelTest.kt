package com.example.e_store.features.settings.view_model

import androidx.test.runner.AndroidJUnitRunner
import com.example.e_store.features.search.view_model.SearchViewModel
import com.example.e_store.utils.data_layer.FakeEStoreRepositoryEmp
import com.example.e_store.utils.shared_models.CurrencyResponse
import com.example.e_store.utils.shared_models.DataState
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.test_utils.CurrencyResponseMockModel
import com.example.e_store.utils.test_utils.ProductMockModel
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class SettingsViewModelTest{
    lateinit var fakeRepository: FakeEStoreRepositoryEmp
    lateinit var viewModel: SettingsViewModel
    lateinit var currencyResponse: List<CurrencyResponse>



    //given
    @Before
    fun setUp() {
        fakeRepository = FakeEStoreRepositoryEmp()
        viewModel = SettingsViewModel(fakeRepository)
        currencyResponse = CurrencyResponseMockModel.currencyResponseList
    }



    @Test
    fun fetchCurrencyResponse_setCurrencyResponseStateToSuccess() = runTest {
        viewModel.fetchCurrency()

        advanceUntilIdle()
        val currencyResponseState = viewModel.currencyResponse.value
        assertTrue(currencyResponseState is DataState.Success)
        val currencies = (currencyResponseState as DataState.Success).data
        TestCase.assertEquals(currencyResponse.get(0), currencies)
    }

    @Test
    fun fetchCurrencyResponse_setsCurrencyResponseStateToLoading() = runTest {
        viewModel.fetchCurrency()
        val currencyResponseState = viewModel.currencyResponse.value
        assertTrue(currencyResponseState is DataState.Loading)
    }

    @Test
    fun fetchCurrencyResponse_setsCurrencyResponseToError() = runTest {
        fakeRepository.shouldThrowError = true // Simulate an error condition
        viewModel.fetchCurrency() // Call the method under test
        advanceUntilIdle() // Allow any coroutines to finish executing
        val currencyResponseState = viewModel.currencyResponse.value // Get the current state
        assertTrue(currencyResponseState is DataState.Error) // Check if it's an error state
        assertEquals(2131820840, (currencyResponseState as DataState.Error).message) // Validate the error message
    }

    @Test
    fun fetchCurrencyResponse_setsCurrencyResponseStateToEmptySuccess() = runTest {
        fakeRepository.IsEmpty = true
        viewModel.fetchCurrency()
        advanceUntilIdle()
        val currencyResponseState = viewModel.currencyResponse.value
        assertTrue(currencyResponseState is DataState.Error)
        assertEquals(2131820840, (currencyResponseState as DataState.Error).message)
    }


}