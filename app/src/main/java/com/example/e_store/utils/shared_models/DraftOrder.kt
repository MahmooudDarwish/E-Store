package com.example.e_store.utils.shared_models


//data class ShoppingCartDraftOrder(
//    val line_items: List<LineItem>,
//    val customer: Customer,
//    val note: String? = null,       // Optional: Notes for the order
//    val tags: String? = null        // Optional: Tags to categorize the order
//)


data class AppliedDiscount(
    val description: String? = null,
    var value: String? = null,         // Assuming the value is a string for monetary values
    val title: String? = null,
    val value_type: String? = null      // Assuming the value_type is a string
)
/*
"applied_discount": {
    "description": "Summer Sale Discount",
    "value": "10.00",
    "title": "10% off",
    "amount": "10.00",
    "value_type": "fixed_amount"
}
*/


data class LineItem(
    val id: Long? = null,
    val product_id: Long? = null,  // Shopify draft order ID
    val title: String,              // The product name
    val price: String,              // The price of the product
    var quantity: Int,              // The quantity of the product
    val variant_id: String,           // Shopify variant ID (for color/size variants)
    val properties: List<Property> // Additional properties like size, color
)

data class Property(
    val name: String,
    val value: String
)


data class DraftOrderDetails(
    val id: Long? = null,
    var line_items: List<LineItem>,  // Items in the draft order
    val email: String,          // Customer details
    val note: String? = null,       // Optional: Notes for the order
    val tags: String? = null, // Optional: Tags to categorize the order
    val invoice_url: String? = null,
    var applied_discount: AppliedDiscount? = null,
    val total_price: String? = null,
    val subtotal_price: String? = null,
    val total_tax: String? = null,
    var shipping_address: Address? = null,
    var billing_address: Address? = null,
)

object DraftOrderIDHolder{
    var draftOrderId: Long? = null
    var total_price : String? = null
}


data class DraftOrderResponse(
    val draft_orders: List<DraftOrderDetails>
)

data class SingleDraftOrderResponse(
    val draft_order: DraftOrderDetails
)

data class DraftOrderRequest(
    val draft_order: DraftOrderDetails
)


data class SingleAddressResponse(
   val customer_address : Address
)
data class CustomerResponse(
    val customers: List<Customer>
)

data class CustomerRequest(
    val customer: Customer
)


data class Customer(
    val first_name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val id: Long? = null,
    val last_name: String? = null,
    val verified_email: Boolean = true,
    val send_email_welcome: Boolean = true,
    val addresses : List<Address>? = null
)

data class AddNewAddress(
    val address: Address
)

data class AddressResponse(
    val addresses: List<Address>
)


data class Address(
    val id: Long? = null,
    val address1: String? = null,
    val city: String? = null,
    val province: String? = null,
    val phone: String? = null,
    val zip: String? = null,
    val last_name: String? = null,
    val first_name: String? = null,
    val country: String? = null,
    var default: Boolean? = null,
    val customer_id : Long? = null,
    var country_code : String? = null
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
