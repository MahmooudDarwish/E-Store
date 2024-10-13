package com.example.e_store.utils.test_utils

import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.DiscountCode
import com.example.e_store.utils.shared_models.Image

class DiscountCodeModel {
    companion object {
        val discountCode1 = DiscountCode(
            id = 1,
            price_rule_id = 101,
            code = "SAVE20",
            usage_count = 5,
            starts_at = "2024-10-01T00:00:00Z",
            ends_at = "2024-12-31T23:59:59Z",
            created_at = "2024-09-15T12:00:00Z",
            updated_at = "2024-09-20T12:00:00Z"
        )

        val discountCode2 = DiscountCode(
            id = 2,
            price_rule_id = 102,
            code = "FREESHIP",
            usage_count = 15,
            starts_at = "2024-10-05T00:00:00Z",
            ends_at = "2024-11-30T23:59:59Z",
            created_at = "2024-09-18T12:00:00Z",
            updated_at = "2024-09-25T12:00:00Z"
        )

        val discountCode3 = DiscountCode(
            id = 3,
            price_rule_id = null, // No associated price rule
            code = "HALFOFF",
            usage_count = 0,
            starts_at = "2024-11-01T00:00:00Z",
            ends_at = "2024-11-30T23:59:59Z",
            created_at = "2024-09-20T12:00:00Z",
            updated_at = "2024-09-20T12:00:00Z"
        )

       val discountCode4 = DiscountCode(
        id = 4,
        price_rule_id = 103,
        code = "WELCOME10",
        usage_count = 3,
        starts_at = "2024-09-01T00:00:00Z",
        ends_at = "2024-12-01T23:59:59Z",
        created_at = "2024-08-15T12:00:00Z",
        updated_at = "2024-08-20T12:00:00Z"
        )
       val discountCode5 = DiscountCode(
        id = 5,
        price_rule_id = 104,
        code = "STUDENT5",
        usage_count = 12,
        starts_at = "2024-10-10T00:00:00Z",
        ends_at = "2024-11-15T23:59:59Z",
        created_at = "2024-09-25T12:00:00Z",
        updated_at = "2024-09-30T12:00:00Z"
        )
       val discountCode6 = DiscountCode(
        id = 6,
        price_rule_id = 105,
        code = "BLACKFRIDAY",
        usage_count = 20,
        starts_at = "2024-11-24T00:00:00Z",
        ends_at = "2024-11-30T23:59:59Z",
        created_at = "2024-09-30T12:00:00Z",
        updated_at = "2024-10-05T12:00:00Z"
        )
      val  discountCode7 = DiscountCode(
        id = 7,
        price_rule_id = 106,
        code = "BFCM10",
        usage_count = 1,
        starts_at = "2024-11-20T00:00:00Z",
        ends_at = "2024-11-29T23:59:59Z",
        created_at = "2024-09-28T12:00:00Z",
        updated_at = "2024-10-03T12:00:00Z"
        )
       val  discountCode8 = DiscountCode(
        id = 8,
        price_rule_id = null, // No associated price rule
        code = "CYBERMONDAY",
        usage_count = 8,
        starts_at = "2024-11-30T00:00:00Z",
        ends_at = "2024-12-07T23:59:59Z",
        created_at = "2024-10-01T12:00:00Z",
        updated_at = "2024-10-05T12:00:00Z"
        )
       val  discountCode9 = DiscountCode(
        id = 9,
        price_rule_id = 107,
        code = "XMAS25",
        usage_count = 0,
        starts_at = "2024-12-01T00:00:00Z",
        ends_at = "2024-12-25T23:59:59Z",
        created_at = "2024-10-10T12:00:00Z",
        updated_at = "2024-10-15T12:00:00Z"
        )
      val  discountCode10 =  DiscountCode(
        id = 10,
        price_rule_id = 108,
        code = "NEWYEAR2025",
        usage_count = 2,
        starts_at = "2024-12-26T00:00:00Z",
        ends_at = "2025-01-01T23:59:59Z",
        created_at = "2024-11-01T12:00:00Z",
        updated_at = "2024-11-10T12:00:00Z"
        )


        var discountCodes = listOf(discountCode1, discountCode2, discountCode3)

    }


}