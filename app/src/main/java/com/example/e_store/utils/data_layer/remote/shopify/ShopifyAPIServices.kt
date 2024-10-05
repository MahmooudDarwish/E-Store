package com.example.e_store.utils.data_layer.remote.shopify

import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.ProductResponse
import com.example.e_store.utils.shared_models.SmartCollectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Path
import com.example.e_store.utils.shared_models.CustomCollectionResponse
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.ShoppingCartDraftOrder
import com.example.e_store.utils.shared_models.ShoppingCartDraftOrderDetails
import com.example.e_store.utils.shared_models.ShoppingCartDraftOrderResponse
import com.example.e_store.utils.shared_models.PriceRuleResponse
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




    //shop cart draft order
    @POST(APIKeys.SHOP_CART_DRAFT_ORDERS_ENDPOINT)
    suspend fun createShoppingCartDraftOrder(@Body shoppingCartDraftOrder: ShoppingCartDraftOrder)

    @PUT(APIKeys.SHOP_CART_DRAFT_ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun updateShoppingCartDraftOrder(
        @Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long,
        @Body shoppingCartDraftOrder: ShoppingCartDraftOrder
    )

    @GET(APIKeys.SHOP_CART_DRAFT_ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun fetchShoppingCartDraftOrderByID(@Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long): ShoppingCartDraftOrderResponse

    @DELETE(APIKeys.SHOP_CART_DRAFT_ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun removeShoppingCartDraftDraftOrder(@Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long)

    @GET(APIKeys.SHOP_CART_DRAFT_ORDERS_ENDPOINT)
    suspend fun fetchAllShoppingCartDraftDraftOrdersByCustomerId(
        @Query(APIKeys.CUSTOMER_ID_PARAM) customerId: String
    ): List<ShoppingCartDraftOrderDetails>


}