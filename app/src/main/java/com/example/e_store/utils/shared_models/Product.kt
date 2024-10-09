package com.example.e_store.utils.shared_models

import com.google.gson.annotations.SerializedName
data class ProductResponse(
    val products: List<Product>

)
data class SingleProductResponse(
    val product: Product
)

data class Product(
    val id: Long,
    val title: String,
    val vendor: String,

    @SerializedName("body_html")
    val description:String,

    @SerializedName("product_type")
    val productType: String,

    @SerializedName("created_at")
    val createdAt: String,

    val handle: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("published_at")
    val publishedAt: String?,

    @SerializedName("template_suffix")
    val templateSuffix: String?,

    @SerializedName("published_scope")
    val publishedScope: String,

    val tags: String?,

    val status: String,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String,

    val variants: List<Variant>,

    val options: List<Option>,

    val images: List<ProductImage>,

    val image: ProductImage
)

data class Variant(
    @SerializedName("id")
    val id: Long,

    @SerializedName("product_id")
    val productId: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("position")
    val position: Int,

    @SerializedName("inventory_policy")
    val inventoryPolicy: String,

    @SerializedName("compare_at_price")
    val compareAtPrice: String?,

    @SerializedName("option1")
    val option1: String?,

    @SerializedName("option2")
    val option2: String?,

    @SerializedName("option3")
    val option3: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("taxable")
    val taxable: Boolean,

    @SerializedName("barcode")
    val barcode: String?,

    @SerializedName("fulfillment_service")
    val fulfillmentService: String,

    @SerializedName("grams")
    val grams: Int,

    @SerializedName("inventory_management")
    val inventoryManagement: String?,

    @SerializedName("requires_shipping")
    val requiresShipping: Boolean,

    @SerializedName("sku")
    val sku: String,

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("weight_unit")
    val weightUnit: String,

    @SerializedName("inventory_item_id")
    val inventoryItemId: Long,

    @SerializedName("inventory_quantity")
    val inventoryQuantity: Int,

    @SerializedName("old_inventory_quantity")
    val oldInventoryQuantity: Int,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String,

    @SerializedName("image_id")
    val imageId: Long?
)

data class Option(
    @SerializedName("id")
    val id: Long,

    @SerializedName("product_id")
    val productId: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("position")
    val position: Int,

    @SerializedName("values")
    val values: List<String>
)

data class ProductImage(
    @SerializedName("id")
    val id: Long,

    @SerializedName("alt")
    val alt: String?,

    @SerializedName("position")
    val position: Int,

    @SerializedName("product_id")
    val productId: Long,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("admin_graphql_api_id")
    val adminGraphqlApiId: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("src")
    val src: String,

    @SerializedName("variant_ids")
    val variantIds: List<Long>
)
