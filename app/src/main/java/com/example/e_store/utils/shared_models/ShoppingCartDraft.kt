package com.example.e_store.utils.shared_models

data class ShoppingCartDraftOrder(
    val line_items: List<LineItem>,
    val customer: Customer,
    val note: String? = null,       // Optional: Notes for the order
    val tags: String? = null        // Optional: Tags to categorize the order
)
data class LineItem(
    val title: String,              // The product name
    val price: String,              // The price of the product
    val quantity: Int,              // The quantity of the product
    val variant_id: Long,           // Shopify variant ID (for color/size variants)
    val properties: Map<String, String>? = null // Additional properties like size, color
)
data class Customer(
    val id: String? = null,         // Firebase UID or Shopify customer ID
    val email: String? = null       // Optional: Customer's email
)
data class ShoppingCartDraftOrderResponse(
    val draft_order: ShoppingCartDraftOrderDetails
)


data class ShoppingCartDraftOrderDetails(
    val id: Long,                   // Shopify draft order ID
    val line_items: List<LineItem>,  // Items in the draft order
    val customer: Customer,          // Customer details
    val total_price: String          // Total price for the draft order
)

/*
// Create a list of LineItem objects (cart items)
val lineItems = listOf(
    LineItem(
        title = "T-shirt",
        price = "20.00",
        quantity = 2,
        variant_id = 12345678,
        properties = mapOf("size" to "M", "color" to "Red")
    )
)

// Create the customer object using Firebase UID
val customer = Customer(id = "firebase_user_id_12345")

// Create the DraftOrder object
val draftOrder = DraftOrder(
    line_items = lineItems,
    customer = customer,
    note = "Draft order for Firebase user xyz123"
)*/
