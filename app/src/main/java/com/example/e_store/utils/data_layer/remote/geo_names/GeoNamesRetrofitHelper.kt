package com.example.e_store.utils.data_layer.remote.geo_names

import com.example.e_store.utils.constants.APIKeys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeoNamesRetrofitHelper {

    val api: GeoNamesApi by lazy {
        val clientBuilder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        clientBuilder.addInterceptor(logging)

        val client = clientBuilder.build()

        Retrofit.Builder()
            .baseUrl(APIKeys.GEONAMES_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoNamesApi::class.java)
    }
}