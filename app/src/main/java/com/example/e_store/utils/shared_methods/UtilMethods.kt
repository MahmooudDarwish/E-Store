package com.example.e_store.utils.shared_methods

import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.ProductDetails

fun initializeProductDetails(product: Product) {
    ProductDetails.id = product.id
    ProductDetails.title = product.title
    ProductDetails.vendor = product.vendor
    ProductDetails.price = product.variants[0].price
    //ProductDetails.description = product.bodyHtml
    ProductDetails.stock = product.variants[0].inventoryQuantity
    ProductDetails.images = product.images.map { it.src }
    ProductDetails.colors = product.options[1].values
    ProductDetails.sizes = product.options[0].values
    ProductDetails.variants = product.variants
}