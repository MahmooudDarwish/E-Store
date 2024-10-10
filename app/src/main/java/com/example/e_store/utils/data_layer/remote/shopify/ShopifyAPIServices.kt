package com.example.e_store.utils.data_layer.remote.shopify

import OrderResponse
import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.ProductResponse
import com.example.e_store.utils.shared_models.SmartCollectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Path
import com.example.e_store.utils.shared_models.CustomCollectionResponse
import com.example.e_store.utils.shared_models.CustomerRequest
import com.example.e_store.utils.shared_models.CustomerResponse
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.PriceRuleResponse
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.SingleDraftOrderResponse
import com.example.e_store.utils.shared_models.SinglePriceRuleResponse
import com.example.e_store.utils.shared_models.SingleProductResponse
import kotlinx.coroutines.flow.Flow
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

    @PUT(APIKeys.COMPLETE_DRAFT_ORDERS_ENDPOINT)
    fun updateDraftOrderToCompleteDraftOrder(
        @Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long
    )


    //shop cart draft order
    @POST(APIKeys.DRAFT_ORDERS_ENDPOINT)
    suspend fun createDraftOrder(@Body shoppingCartDraftOrder: DraftOrderRequest)

    @PUT(APIKeys.ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun updateDraftOrder(
        @Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long,
        @Body shoppingCartDraftOrder: DraftOrderRequest
    )

    @GET(APIKeys.PRODUCT_ID_ENDPOINT)
    suspend fun fetchProduct(
        @Path(APIKeys.PRODUCT_ID_PARAM) productId: Long
    ): SingleProductResponse


    @GET(APIKeys.ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun fetchDraftOrderByID(@Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long): SingleDraftOrderResponse

    @DELETE(APIKeys.ORDERS_DRAFT_ORDER_ID_ENDPOINT)
    suspend fun removeDraftDraftOrder(@Path(APIKeys.DRAFT_ORDER_ID_PARAM) draftOrderId: Long)

    @GET(APIKeys.DRAFT_ORDERS_ENDPOINT)
    suspend fun fetchAllDraftOrders(
    ): DraftOrderResponse

    @GET(APIKeys.PRICING_RULES_ENDPOINT_ID)
    suspend fun fetchPricingRuleByID(@Path(APIKeys.PRICE_RULE_ID_PARAM) priceRuleId: Long): SinglePriceRuleResponse


    @GET(APIKeys.CUSTOMER_ENDPOINT)
    suspend fun fetchAllCustomers(): CustomerResponse


    @GET(APIKeys.CUSTOMER_ENDPOINT_ID)
    suspend fun fetchCustomer(@Path(APIKeys.CUSTOMER_ID_PARAM) customerId: Long): CustomerResponse


    @POST(APIKeys.CUSTOMER_ENDPOINT)
    suspend fun createCustomer(@Body customer: CustomerRequest)

    @PUT(APIKeys.CUSTOMER_ENDPOINT_ID)
    suspend fun updateCustomer(
        @Path(APIKeys.CUSTOMER_ID_PARAM) customerId: Long,
        @Body customer: CustomerRequest
    )


    @GET(APIKeys.CUSTOMER_ADDRESS_ENDPOINT)
    suspend fun fetchCustomerAddresses(@Path(APIKeys.CUSTOMER_ID_PARAM) customerId: Long): AddressResponse

    @POST(APIKeys.CUSTOMER_ADDRESS_ENDPOINT)
    suspend fun createCustomerAddress(
        @Path(APIKeys.CUSTOMER_ID_PARAM) customerId: Long,
        @Body address: AddNewAddress
    )


    @DELETE(APIKeys.DELETE_CUSTOMER_ADDRESS_ENDPOINT)
    suspend fun deleteCustomerAddress(
        @Path(APIKeys.CUSTOMER_ID_PARAM) customerId: Long,
        @Path(APIKeys.ADDRESS_ID_PARAM) addressId: Long
    )


    @PUT(APIKeys.DELETE_CUSTOMER_ADDRESS_ENDPOINT)
    suspend fun updateCustomerAddress(
        @Path(APIKeys.CUSTOMER_ID_PARAM) customerId: Long,
        @Path(APIKeys.ADDRESS_ID_PARAM) addressId: Long,
        @Body address: AddNewAddress
    )

}