package com.example.e_store.utils.shared_models

data class Brand(
    val id: Long,
    val handle: String,
    val title: String,
    val image: Image?
)
data class Image(
    val src: String
)

data class SmartCollectionsResponse(
    val smart_collections: List<Brand>
)
