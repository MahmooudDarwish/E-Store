package com.example.e_store.utils.data_layer.remote.exchange_rate

import com.example.e_store.utils.shared_models.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("latest/{currency}")
    suspend fun getLatestExchangeRates(
        @Path("currency") currency: String = "USD"
    ): CurrencyResponse
}

