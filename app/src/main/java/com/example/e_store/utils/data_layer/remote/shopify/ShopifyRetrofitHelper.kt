package com.example.e_store.utils.data_layer.remote.shopify

import com.airbnb.lottie.compose.BuildConfig
import com.example.e_store.utils.constants.APIKeys
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ShopifyRetrofitHelper {

    val api: ShopifyAPIServices by lazy {

        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(APIKeys.ACCESS_TOKEN_HEADER, APIKeys.ADMIN_API_ACCESS_TOKEN)
                .build()
            chain.proceed(request)
        }

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(logging)
        }

        val client = clientBuilder.build()

        Retrofit.Builder()
            .baseUrl(APIKeys.SHOPIFY_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShopifyAPIServices::class.java)
    }
}





