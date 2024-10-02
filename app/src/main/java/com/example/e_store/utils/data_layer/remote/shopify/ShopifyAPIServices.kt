package com.example.e_store.utils.data_layer.remote.shopify

import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.SmartCollectionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ShopifyAPIServices {
    @GET(APIKeys.SMART_COLLECTION_ENDPOINT)
    @Headers("${APIKeys.ACCESS_TOKEN_HEADER}: ${APIKeys.ADMIN_API_ACCESS_TOKEN}")
    suspend fun getBrands(): Response<SmartCollectionsResponse>
}