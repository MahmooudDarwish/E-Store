package com.example.e_store.utils.data_layer.remote.exchange_rate

import com.example.e_store.utils.constants.APIKeys
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExchangeRateRetrofitHelper {
    private const val BASE_URL =
        "https://v6.exchangerate-api.com/v6/${APIKeys.EXCHANGE_RATE_API_KEY}/"

   fun getInstance(): Retrofit {
       return Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
   }

}
