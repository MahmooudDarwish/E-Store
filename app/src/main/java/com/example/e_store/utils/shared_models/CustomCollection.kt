package com.example.e_store.utils.shared_models

import com.google.gson.annotations.SerializedName


data class CustomCollectionResponse(
    @SerializedName("custom_collections")
    val customCollections: List<CustomCollection>
)

data class CustomCollection(
    val id: Long,
    val handle: String,
    val title: String,
)

