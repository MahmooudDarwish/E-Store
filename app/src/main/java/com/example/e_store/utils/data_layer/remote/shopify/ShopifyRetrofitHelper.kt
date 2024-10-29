@file:Suppress("DEPRECATION")

package com.example.e_store.utils.data_layer.remote.shopify

import android.content.Context
import android.net.ConnectivityManager
import com.example.e_store.utils.constants.APIKeys
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ShopifyRetrofitHelper {

    private const val cacheSize = 5 * 1024 * 1024
    private var retrofit: Retrofit? = null

    fun createOkHttpClient(context: Context): OkHttpClient {
        val myCache = Cache(File(context.cacheDir, "http_cache"), cacheSize.toLong())

        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(APIKeys.ACCESS_TOKEN_HEADER, APIKeys.ADMIN_API_ACCESS_TOKEN)
                .build()
            chain.proceed(request)
        }

        val offlineInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!hasNetwork(context)) {
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                    .build()
            }
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor(headerInterceptor)
            .addInterceptor(logging)
            .addInterceptor(offlineInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    fun getApi(context: Context): ShopifyAPIServices {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(APIKeys.SHOPIFY_BASE_URL)
                .client(createOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ShopifyAPIServices::class.java)
    }

    private fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
