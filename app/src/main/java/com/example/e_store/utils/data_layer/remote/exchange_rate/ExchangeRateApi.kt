package com.example.e_store.utils.data_layer.remote.exchange_rate

import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET(APIKeys.LATEST_CURRENCY_ENDPOINT)
    suspend fun getLatestExchangeRates(
        @Path(APIKeys.CURRENCY_PARAM) currency: String = "USD"
    ): CurrencyResponse
}

