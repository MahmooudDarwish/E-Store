package com.example.e_store.utils.data_layer.remote.shopify

import com.airbnb.lottie.compose.BuildConfig
import com.example.e_store.utils.constants.APIKeys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ShopifyRetrofitHelper {
    val api: ShopifyAPIServices by lazy {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logging).build()


        Retrofit.Builder()
            .baseUrl(APIKeys.SHOPIFY_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShopifyAPIServices::class.java)
    }

}





