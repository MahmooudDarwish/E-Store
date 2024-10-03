package com.example.e_store.utils.constants

object APIKeys {
    /// Shopify API Keys
    // Init setup
    const val SHOPIFY_API_KEY = "6f72d418fcb2c0867c10ff051079310e"
    const val ADMIN_API_ACCESS_TOKEN = "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"
    const val STORE_URL = "android-sv24-r3team3.myshopify.com"
    const val SHOPIFY_BASE_URL = "https://$SHOPIFY_API_KEY:$ADMIN_API_ACCESS_TOKEN@$STORE_URL/admin/api/2024-07/"
    const val SHOPIFY_API_SECRET_KEY = "2d54b067f29fe607ccecd2c91294acb3"
    const val STOREFRONT_API_ACCESS_TOKEN = "7d0ecff0b6c5a01e2350a2c44428ae0f"

    //Headers
    const val ACCESS_TOKEN_HEADER = "X-Shopify-Access-Token"

    //Endpoints
    const val SMART_COLLECTION_ENDPOINT = "smart_collections.json"
    const val CUSTOM_COLLECTION_ENDPOINT = "custom_collections.json"
    const val PRODUCTS_ENDPOINT = "products.json"


    //Values

    const val HOME = "Home"
}




