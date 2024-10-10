package com.example.e_store.utils.shared_models

data class CurrencyResponse(
    val result: String,
    val documentation: String,
    val terms_of_use: String,
    val time_last_update_unix: Long,
    val time_last_update_utc: String,
    val time_next_update_unix: Long,
    val time_next_update_utc: String,
    val base_code: String,
    val conversion_rates: ConversionRates
)

data class ConversionRates(
    val USD: Double,
    val AUD: Double,
    val BGN: Double,
    val CAD: Double,
    val CHF: Double,
    val CNY: Double,
    val EGP: Double,
    val EUR: Double,
    val GBP: Double
)
