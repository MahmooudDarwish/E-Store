package com.example.e_store.utils.test_utils

import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.Image

class BrandsMockModel {
    companion object {
        val brand1 = Brand(
            id = 1,
            handle = "brand-1",
            title = "Brand 1",
            image = Image(src = "https://example.com/brand1.jpg")
        )
        val brand2 = Brand(
            id = 2,
            handle = "brand-2",
            title = "Brand 2",
            image = Image(src = "https://example.com/brand2.jpg")
        )
        val brand3 = Brand(
            id = 3,
            handle = "brand-3",
            title = "Brand 3",
            image = Image(src = "https://example.com/brand3.jpg")
        )
        val brand4 = Brand(
            id = 4,
            handle = "brand-4",
            title = "Brand 4",
            image = Image(src = "https://example.com/brand4.jpg")
        )

        val brand5 = Brand(
            id = 4,
            handle = "brand-5",
            title = "Brand 5",
            image = Image(src = "https://example.com/brand4.jpg")
        )
        val brandWithoutImage = Brand(
            id = 5,
            handle = "brand-without-image",
            title = "Brand Without Image",
            image = null
        )


        var brands = listOf(brand1, brand2, brand3, brand4)
    }


}