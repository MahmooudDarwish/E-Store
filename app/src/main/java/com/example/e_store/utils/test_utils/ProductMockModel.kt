package com.example.e_store.utils.test_utils

import com.example.e_store.utils.shared_models.Option
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.ProductImage
import com.example.e_store.utils.shared_models.Variant

object ProductMockModel {
    val product1 = Product(
        id = 1,
        title = "Product 1",
        vendor = "brand-1",
        description = "Description for Product 1",
        productType = "Type 1",
        createdAt = "2024-01-01T00:00:00",
        handle = "product-1",
        updatedAt = "2024-01-02T00:00:00",
        publishedAt = "2024-01-03T00:00:00",
        templateSuffix = null,
        publishedScope = "global",
        tags = "tag1, tag2",
        status = "active",
        adminGraphqlApiId = "gid://shopify/Product/1",
        variants = listOf(
            Variant(
                id = 101,
                productId = 1,
                title = "Variant 1",
                price = "10.00",
                position = 1,
                inventoryPolicy = "continue",
                compareAtPrice = "12.00",
                option1 = "Option 1",
                option2 = null,
                option3 = null,
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-02T00:00:00",
                taxable = true,
                barcode = "1234567890",
                fulfillmentService = "manual",
                grams = 100,
                inventoryManagement = "shopify",
                requiresShipping = true,
                sku = "SKU001",
                weight = 0.5,
                weightUnit = "kg",
                inventoryItemId = 1001,
                inventoryQuantity = 50,
                oldInventoryQuantity = 45,
                adminGraphqlApiId = "gid://shopify/Variant/101",
                imageId = null
            )
        ),
        options = listOf(
            Option(
                id = 201,
                productId = 1,
                name = "Size",
                position = 1,
                values = listOf("Small", "Medium", "Large")
            )
        ),
        images = listOf(
            ProductImage(
                id = 301,
                alt = "Image for Product 1",
                position = 1,
                productId = 1,
                createdAt = "2024-01-01T00:00:00",
                updatedAt = "2024-01-02T00:00:00",
                adminGraphqlApiId = "gid://shopify/ProductImage/301",
                width = 800,
                height = 600,
                src = "https://example.com/product1.jpg",
                variantIds = emptyList()
            )
        ),
        image = ProductImage(
            id = 301,
            alt = "Main image for Product 1",
            position = 1,
            productId = 1,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-02T00:00:00",
            adminGraphqlApiId = "gid://shopify/ProductImage/301",
            width = 800,
            height = 600,
            src = "https://example.com/product1.jpg",
            variantIds = emptyList()
        )
    )

    val product2 = Product(
        id = 2,
        title = "Product 2",
        vendor = "brand-2",
        description = "Description for Product 2",
        productType = "Type 2",
        createdAt = "2024-02-01T00:00:00",
        handle = "product-2",
        updatedAt = "2024-02-02T00:00:00",
        publishedAt = "2024-02-03T00:00:00",
        templateSuffix = null,
        publishedScope = "global",
        tags = "tag3, tag4",
        status = "active",
        adminGraphqlApiId = "gid://shopify/Product/2",
        variants = listOf(
            Variant(
                id = 102,
                productId = 2,
                title = "Variant 2",
                price = "20.00",
                position = 1,
                inventoryPolicy = "continue",
                compareAtPrice = "22.00",
                option1 = "Option 1",
                option2 = null,
                option3 = null,
                createdAt = "2024-02-01T00:00:00",
                updatedAt = "2024-02-02T00:00:00",
                taxable = true,
                barcode = "0987654321",
                fulfillmentService = "manual",
                grams = 200,
                inventoryManagement = "shopify",
                requiresShipping = true,
                sku = "SKU002",
                weight = 1.0,
                weightUnit = "kg",
                inventoryItemId = 1002,
                inventoryQuantity = 30,
                oldInventoryQuantity = 25,
                adminGraphqlApiId = "gid://shopify/Variant/102",
                imageId = null
            )
        ),
        options = listOf(
            Option(
                id = 202,
                productId = 2,
                name = "Color",
                position = 1,
                values = listOf("Red", "Blue", "Green")
            )
        ),
        images = listOf(
            ProductImage(
                id = 302,
                alt = "Image for Product 2",
                position = 1,
                productId = 2,
                createdAt = "2024-02-01T00:00:00",
                updatedAt = "2024-02-02T00:00:00",
                adminGraphqlApiId = "gid://shopify/ProductImage/302",
                width = 1024,
                height = 768,
                src = "https://example.com/product2.jpg",
                variantIds = emptyList()
            )
        ),
        image = ProductImage(
            id = 302,
            alt = "Main image for Product 2",
            position = 1,
            productId = 2,
            createdAt = "2024-02-01T00:00:00",
            updatedAt = "2024-02-02T00:00:00",
            adminGraphqlApiId = "gid://shopify/ProductImage/302",
            width = 1024,
            height = 768,
            src = "https://example.com/product2.jpg",
            variantIds = emptyList()
        )
    )
    val product3 = Product(
        id = 3,
        title = "Product 3",
        vendor = "brand-3",
        description = "Description for Product 3",
        productType = "Type 3",
        createdAt = "2024-03-01T00:00:00",
        handle = "product-3",
        updatedAt = "2024-03-02T00:00:00",
        publishedAt = "2024-03-03T00:00:00",
        templateSuffix = null,
        publishedScope = "global",
        tags = "tag5, tag6",
        status = "active",
        adminGraphqlApiId = "gid://shopify/Product/3",
        variants = listOf(
            Variant(
                id = 103,
                productId = 3,
                title = "Variant 3",
                price = "30.00",
                position = 1,
                inventoryPolicy = "continue",
                compareAtPrice = "32.00",
                option1 = "Option 1",
                option2 = null,
                option3 = null,
                createdAt = "2024-03-01T00:00:00",
                updatedAt = "2024-03-02T00:00:00",
                taxable = true,
                barcode = "1122334455",
                fulfillmentService = "manual",
                grams = 300,
                inventoryManagement = "shopify",
                requiresShipping = true,
                sku = "SKU003",
                weight = 1.5,
                weightUnit = "kg",
                inventoryItemId = 1003,
                inventoryQuantity = 20,
                oldInventoryQuantity = 15,
                adminGraphqlApiId = "gid://shopify/Variant/103",
                imageId = null
            )
        ),
        options = listOf(
            Option(
                id = 203,
                productId = 3,
                name = "Material",
                position = 1,
                values = listOf("Cotton", "Polyester", "Silk")
            )
        ),
        images = listOf(
            ProductImage(
                id = 303,
                alt = "Image for Product 3",
                position = 1,
                productId = 3,
                createdAt = "2024-03-01T00:00:00",
                updatedAt = "2024-03-02T00:00:00",
                adminGraphqlApiId = "gid://shopify/ProductImage/303",
                width = 640,
                height = 480,
                src = "https://example.com/product3.jpg",
                variantIds = emptyList()
            )
        ),
        image = ProductImage(
            id = 303,
            alt = "Main image for Product 3",
            position = 1,
            productId = 3,
            createdAt = "2024-03-01T00:00:00",
            updatedAt = "2024-03-02T00:00:00",
            adminGraphqlApiId = "gid://shopify/ProductImage/303",
            width = 640,
            height = 480,
            src = "https://example.com/product3.jpg",
            variantIds = emptyList()
        )
    )

    val collections: Map<String, List<Product>> = mapOf(
        "1" to listOf(product1, product3),
        "2" to listOf(product2)
    )
    val products = listOf(product1, product2)



}