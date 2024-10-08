package com.example.e_store.utils.shared_methods

import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.ProductDetails
import java.text.SimpleDateFormat
import java.util.Locale

fun initializeProductDetails(product: Product) {
    ProductDetails.id = product.id
    ProductDetails.title = product.title
    ProductDetails.vendor = product.vendor
    ProductDetails.price = product.variants[0].price
    ProductDetails.description = product.description
    ProductDetails.stock = product.variants[0].inventoryQuantity
    ProductDetails.images = product.images.map { it.src }
    ProductDetails.colors = product.options[1].values
    ProductDetails.sizes = product.options[0].values
    ProductDetails.variants = product.variants
}

fun changeDateFormat(inputDate: String): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    val outputFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    val date = inputFormatter.parse(inputDate)
    return if (date != null) {
        outputFormatter.format(date)
    } else {
        "Invalid Date"
    }
}