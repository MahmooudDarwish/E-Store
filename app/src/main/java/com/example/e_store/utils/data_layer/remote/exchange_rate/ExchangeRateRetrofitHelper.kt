package com.example.e_store.utils.data_layer.remote.exchange_rate

import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.data_layer.remote.geo_names.GeoNamesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ExchangeRateRetrofitHelper {

    val api: ExchangeRateApi by lazy {
        val clientBuilder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(logging)

        val client = clientBuilder.build()

        Retrofit.Builder()
            .baseUrl("${APIKeys.EXCHANGE_RATE_API_URL}${APIKeys.EXCHANGE_RATE_API_KEY}/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }

}
