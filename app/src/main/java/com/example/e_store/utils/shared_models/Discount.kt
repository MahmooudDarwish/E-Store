package com.example.e_store.utils.shared_models
data class DiscountCode(
    val id: Long,
    val price_rule_id: Long? = null,
    val code: String,
    val usage_count: Int,
    val starts_at: String,
    val ends_at: String,
    val created_at: String,
    val updated_at: String
)

data class PriceRule(
    val id: Long,
    val title: String,
    val value_type: String,
    val value: String,
    val created_at: String,
    val updated_at: String
)

data class PriceRuleResponse(
    val price_rules: List<PriceRule>
)

data class SinglePriceRuleResponse(
    val price_rule: PriceRule
)

data class DiscountCodesResponse(
    val discount_codes: List<DiscountCode>
)
