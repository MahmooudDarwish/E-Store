package com.example.e_store.utils.shared_models

object ProductDetails {


    var id: Long = 0L
    var title: String = "Product Name"
    var vendor: String = "Vendor Name"
    var price: String = "Price"
    var description: String = "Product Description"
    var stock: Int = 100
    var images: List<String> = listOf(
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg",
        "https://example.com/image3.jpg"
    )
    var colors: List<String> = listOf("Red", "Blue", "Green")
    var sizes: List<String> = listOf("S", "M", "L", "XL")
    var variants: List<Variant> = listOf(
        Variant(
            id = 1L,
            productId = 101L,
            title = "Variant A",
            price = "19.99",
            position = 1,
            inventoryPolicy = "deny",
            compareAtPrice = "24.99",
            option1 = "Size: M",
            option2 = "Color: Red",
            option3 = null,
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-10T00:00:00Z",
            taxable = true,
            barcode = "1234567890123",
            fulfillmentService = "manual",
            grams = 500,
            inventoryManagement = "shopify",
            requiresShipping = true,
            sku = "SKU-001",
            weight = 0.5,
            weightUnit = "kg",
            inventoryItemId = 201L,
            inventoryQuantity = 10,
            oldInventoryQuantity = 15,
            adminGraphqlApiId = "gid://shopify/ProductVariant/1",
            imageId = 301L
        ),
        Variant(
            id = 2L,
            productId = 101L,
            title = "Variant B",
            price = "29.99",
            position = 2,
            inventoryPolicy = "deny",
            compareAtPrice = "34.99",
            option1 = "Size: L",
            option2 = "Color: Blue",
            option3 = null,
            createdAt = "2023-01-05T00:00:00Z",
            updatedAt = "2023-01-15T00:00:00Z",
            taxable = true,
            barcode = "9876543210987",
            fulfillmentService = "manual",
            grams = 700,
            inventoryManagement = "shopify",
            requiresShipping = true,
            sku = "SKU-002",
            weight = 0.7,
            weightUnit = "kg",
            inventoryItemId = 202L,
            inventoryQuantity = 5,
            oldInventoryQuantity = 8,
            adminGraphqlApiId = "gid://shopify/ProductVariant/2",
            imageId = 302L
        ),
        Variant(
            id = 3L,
            productId = 102L,
            title = "Variant C",
            price = "39.99",
            position = 3,
            inventoryPolicy = "continue",
            compareAtPrice = null,
            option1 = "Size: S",
            option2 = "Color: Green",
            option3 = null,
            createdAt = "2023-02-01T00:00:00Z",
            updatedAt = "2023-02-10T00:00:00Z",
            taxable = true,
            barcode = "1231231231231",
            fulfillmentService = "manual",
            grams = 600,
            inventoryManagement = "shopify",
            requiresShipping = true,
            sku = "SKU-003",
            weight = 0.6,
            weightUnit = "kg",
            inventoryItemId = 203L,
            inventoryQuantity = 0,
            oldInventoryQuantity = 5,
            adminGraphqlApiId = "gid://shopify/ProductVariant/3",
            imageId = 303L
        ),
        Variant(
            id = 4L,
            productId = 103L,
            title = "Variant D",
            price = "49.99",
            position = 4,
            inventoryPolicy = "deny",
            compareAtPrice = "59.99",
            option1 = "Size: XL",
            option2 = "Color: Yellow",
            option3 = null,
            createdAt = "2023-03-01T00:00:00Z",
            updatedAt = "2023-03-10T00:00:00Z",
            taxable = true,
            barcode = "4564564564564",
            fulfillmentService = "manual",
            grams = 800,
            inventoryManagement = "shopify",
            requiresShipping = true,
            sku = "SKU-004",
            weight = 0.8,
            weightUnit = "kg",
            inventoryItemId = 204L,
            inventoryQuantity = 20,
            oldInventoryQuantity = 22,
            adminGraphqlApiId = "gid://shopify/ProductVariant/4",
            imageId = 304L
        )
    )

    var isNavigationFromFavourites: Boolean = false


}