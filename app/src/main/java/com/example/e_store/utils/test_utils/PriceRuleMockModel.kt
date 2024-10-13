package com.example.e_store.utils.test_utils

import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.Customer
import com.example.e_store.utils.shared_models.PriceRule

class PriceRuleMockModel {

    companion object {
        val priceRule1 = PriceRule(
            id = 101,
            title = "20% off on Electronics",
            value_type = "percentage",
            value = "20",
            created_at = "2024-09-10T12:00:00Z",
            updated_at = "2024-09-15T12:00:00Z"
        )

        val priceRule2 = PriceRule(
            id = 102,
            title = "Free Shipping on Orders Over $50",
            value_type = "fixed_amount",
            value = "0", // Free shipping
            created_at = "2024-09-12T12:00:00Z",
            updated_at = "2024-09-18T12:00:00Z"
        )

        val priceRule3 = PriceRule(
            id = 103,
            title = "50% off on Selected Items",
            value_type = "percentage",
            value = "50",
            created_at = "2024-09-15T12:00:00Z",
            updated_at = "2024-09-20T12:00:00Z"
        )
        val priceRules = listOf(priceRule1, priceRule2, priceRule3)

    }




}