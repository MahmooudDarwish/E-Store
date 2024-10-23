package com.example.e_store.utils.constants

import retrofit2.http.GET
import retrofit2.http.Query

object APIKeys {
    /// Shopify API Keys
    // Init setup
    //Headers
    const val ACCESS_TOKEN_HEADER = "X-Shopify-Access-Token"

    //Endpoints
    const val SMART_COLLECTION_ENDPOINT = "smart_collections.json"
    const val CUSTOM_COLLECTION_ENDPOINT = "custom_collections.json"
    const val PRODUCTS_ENDPOINT = "products.json"

    const val PRICING_RULES_ENDPOINT = "price_rules.json"
    const val PRICING_RULES_ENDPOINT_ID = "price_rules/{price_rule_id}.json"
    const val DISCOUNT_CODES_ENDPOINT = "price_rules/{price_rule_id}/discount_codes.json"
    const val COMPLETE_DRAFT_ORDERS_ENDPOINT = "draft_orders/{draft_order_id}/complete.json"
    const val DRAFT_ORDERS_ENDPOINT = "draft_orders.json"
    const val ORDERS_DRAFT_ORDER_ID_ENDPOINT = "draft_orders/{draft_order_id}.json"
    const val ORDERS_ENDPOINT = "orders.json"
    const val PRODUCT_ID_ENDPOINT = "products/{product_id}.json"

    const val CUSTOMER_ENDPOINT = "customers.json"
    const val CUSTOMER_ENDPOINT_ID = "customers/{customer_id}.json"

    const val CUSTOMER_ADDRESS_ENDPOINT = "customers/{customer_id}/addresses.json"
    const val DELETE_UPDATE_GET_CUSTOMER_ADDRESS_ENDPOINT = "customers/{customer_id}/addresses/{address_id}.json"


    //EndPoints Params
    const val PRICE_RULE_ID_PARAM = "price_rule_id"
    const val LIMIT_PARAM = "limit"
    const val PRODUCT_TYPE_PARAM = "product_type"
    const val COLLECTION_ID_PARAM = "collection_id"
    const val DRAFT_ORDER_ID_PARAM = "draft_order_id" //{draft_order_id} for SHOP_CART_DRAFT_ORDERS_ENDPOINT
    const val STATUS_PARAM = "status"
    const val PRODUCT_ID_PARAM = "product_id"
    const val CUSTOMER_ID_PARAM = "customer_id"
    const val ADDRESS_ID_PARAM = "address_id"


    //Values
    const val HOME = "Home"
    const val SHOPPING_CART = "Shopping Cart"



    ///Exchange rate Api
    // Init setup
    const val EXCHANGE_RATE_API_URL = "https://v6.exchangerate-api.com/v6/"
    const val EXCHANGE_RATE_API_KEY = "5c59b533af77272e758ab490"


    //endpoint
     const val LATEST_CURRENCY_ENDPOINT = "latest/{currency}"

    //EndPoints Params
    const val CURRENCY_PARAM = "currency"




    ///GeoNames Api
    // Init setup
    const val GEONAMES_API_URL = "https://secure.geonames.org/"

    //endpoint
    const val GEONAMES_SEARCH_ENDPOINT = "searchJSON"
    const val GEONAMES_COUNTRY_INFO_ENDPOINT = "countryInfoJSON"


    //EndPoints Params
    const val USERNAME_PARAM = "username"
    const val COUNTRY_PARAM = "country"
    const val FEATURE_CLASS_PARAM = "featureClass"
    const val MAX_ROWS_PARAM = "maxRows"

}




