package com.example.e_store.utils.data_layer.remote.shopify

import OrderResponse
import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.ProductResponse
import com.example.e_store.utils.shared_models.SmartCollectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Path
import com.example.e_store.utils.shared_models.CustomCollectionResponse
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.PriceRuleResponse
import com.example.e_store.utils.shared_models.DraftOrderRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT

interface ShopifyAPIServices {
    @GET(APIKeys.SMART_COLLECTION_ENDPOINT)
    suspend fun fetchBrands(): SmartCollectionsResponse

    @GET(APIKeys.PRODUCTS_ENDPOINT)
    suspend fun fetchProducts(
        @Query(APIKeys.LIMIT_PARAM) limit: Int? = null,
        @Query(APIKeys.PRODUCT_TYPE_PARAM) productType: String? = null,
        @Query(APIKeys.COLLECTION_ID_PARAM) collectionId: String? = null,
    ): ProductResponse

    @GET(APIKeys.CUSTOM_COLLECTION_ENDPOINT)
    suspend fun fetchCustomCollections(): CustomCollectionResponse

    @GET(APIKeys.PRICING_RULES_ENDPOINT)
    suspend fun fetchPricingRules(): Response<PriceRuleResponse>

    @GET(APIKeys.DISCOUNT_CODES_ENDPOINT)
    suspend fun fetchDiscountCodes(
        @Path(APIKeys.PRICE_RULE_ID_PARAM) priceRuleId: Long
    ): DiscountCodesResponse

    @GET(APIKeys.ORDERS_ENDPOINT)
    suspend fun fetchOrders(
        @Query(APIKeys.STATUS_PARAM) status: String? = null
    ): OrderResponse


    //shop cart draft order
    @POST(APIKeys.DRAFT_ORDERS_ENDPOINT)
    suspend fun createDraftOrder(@Body shoppingCartDraftOrder: DraftOrderRequest)

    @PUT(APIKeys.ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun updateDraftOrder(
        @Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long,
        @Body shoppingCartDraftOrder: DraftOrderRequest
    )

    @GET(APIKeys.ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun fetchDraftOrderByID(@Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long): DraftOrderResponse

    @DELETE(APIKeys.ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun removeDraftDraftOrder(@Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long)

    @GET(APIKeys.DRAFT_ORDERS_ENDPOINT)
    suspend fun fetchAllDraftOrders(
    ): DraftOrderResponse


}