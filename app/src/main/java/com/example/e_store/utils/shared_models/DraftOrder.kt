package com.example.e_store.utils.shared_models


//data class ShoppingCartDraftOrder(
//    val line_items: List<LineItem>,
//    val customer: Customer,
//    val note: String? = null,       // Optional: Notes for the order
//    val tags: String? = null        // Optional: Tags to categorize the order
//)


data class LineItem(
    val id :Long?=null,
    val product_id: Long? = null,  // Shopify draft order ID
    val title: String,              // The product name
    val price: String,              // The price of the product
    var quantity: Int,              // The quantity of the product
    val variant_id: String,           // Shopify variant ID (for color/size variants)
    val properties:List<Property> // Additional properties like size, color


)
data class Property(
    val name: String,
    val value: String
)

data class DraftOrderResponse(
    val draft_orders: List<DraftOrderDetails>
)


data class DraftOrderRequest(
    val draft_order: DraftOrderDetails
)

data class DraftOrderDetails(
    val id: Long?=null,
    var line_items: List<LineItem>,  // Items in the draft order
    val email: String,          // Customer details
    val note: String? = null,       // Optional: Notes for the order
    val tags: String? = null, // Optional: Tags to categorize the order
    val invoice_url: String? = null
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
