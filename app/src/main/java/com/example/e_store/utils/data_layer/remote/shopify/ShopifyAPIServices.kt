package com.example.e_store.utils.data_layer.remote.shopify

import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.ProductResponse
import com.example.e_store.utils.shared_models.SmartCollectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Path
import com.example.e_store.utils.shared_models.CustomCollectionResponse
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.PriceRuleResponse
interface ShopifyAPIServices {
    @GET(APIKeys.SMART_COLLECTION_ENDPOINT)
    suspend fun fetchBrands(): SmartCollectionsResponse

    @GET(APIKeys.CUSTOM_COLLECTION_ENDPOINT)
    suspend fun fetchCollections(): CustomCollectionResponse

    @GET(APIKeys.PRODUCTS_ENDPOINT)
    suspend fun fetchProducts(
        @Query("limit") limit: Int? = null,
        @Query("product_type") productType: String? = null,
        @Query("collection_id") collectionId: String? = null,
    ): ProductResponse
    @GET(APIKeys.PRICING_RULES_ENDPOINT)
    suspend fun fetchPricingRules(): Response<PriceRuleResponse>

    @GET(APIKeys.DISCOUNT_CODES_ENDPOINT)
    suspend fun fetchDiscountCodes(
        @Path(APIKeys.PRICE_RULE_ID_PARAM) priceRuleId: Long
    ): DiscountCodesResponse

    @GET(APIKeys.CUSTOM_COLLECTION_ENDPOINT)
    suspend fun fetchCustomCollections(): CustomCollectionResponse



}