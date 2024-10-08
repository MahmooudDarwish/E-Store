package com.example.e_store.utils.shared_models

object ProductDetails {



    var id :Long = 0L
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
    var variants: List<Variant> = listOf()
    var isNavigationFromFavourites: Boolean = false


}