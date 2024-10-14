package com.example.e_store.utils.test_utils

import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.ConversionRates
import com.example.e_store.utils.shared_models.CurrencyResponse

class CurrencyResponseMockModel {

    companion object {
       val currencyResponse1= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs1",
        terms_of_use = "https://example.com/terms1",
        time_last_update_unix = 1633024801L,
        time_last_update_utc = "2021-09-30T14:00:01Z",
        time_next_update_unix = 1633111201L,
        time_next_update_utc = "2021-10-01T14:00:01Z",
        base_code = "USD",
        conversion_rates = ConversionRates(USD = 1.0, AUD = 1.34, BGN = 1.67, CAD = 1.24, CHF = 0.92, CNY = 6.44, EGP = 15.66, EUR = 0.85, GBP = 0.73)
        )
       val currencyResponse2= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs2",
        terms_of_use = "https://example.com/terms2",
        time_last_update_unix = 1633024802L,
        time_last_update_utc = "2021-09-30T14:00:02Z",
        time_next_update_unix = 1633111202L,
        time_next_update_utc = "2021-10-01T14:00:02Z",
        base_code = "EUR",
        conversion_rates = ConversionRates(USD = 1.18, AUD = 1.5, BGN = 1.98, CAD = 1.36, CHF = 1.02, CNY = 7.0, EGP = 18.0, EUR = 1.0, GBP = 0.87)
        )
       val currencyResponse3= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs3",
        terms_of_use = "https://example.com/terms3",
        time_last_update_unix = 1633024803L,
        time_last_update_utc = "2021-09-30T14:00:03Z",
        time_next_update_unix = 1633111203L,
        time_next_update_utc = "2021-10-01T14:00:03Z",
        base_code = "GBP",
        conversion_rates = ConversionRates(USD = 1.38, AUD = 1.85, BGN = 2.35, CAD = 1.55, CHF = 1.19, CNY = 8.0, EGP = 20.0, EUR = 1.17, GBP = 1.0)
        )
       val currencyResponse4= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs4",
        terms_of_use = "https://example.com/terms4",
        time_last_update_unix = 1633024804L,
        time_last_update_utc = "2021-09-30T14:00:04Z",
        time_next_update_unix = 1633111204L,
        time_next_update_utc = "2021-10-01T14:00:04Z",
        base_code = "JPY",
        conversion_rates = ConversionRates(USD = 0.009, AUD = 0.012, BGN = 0.015, CAD = 0.011, CHF = 0.008, CNY = 0.06, EGP = 0.11, EUR = 0.007, GBP = 0.006)
        )
       val currencyResponse5= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs5",
        terms_of_use = "https://example.com/terms5",
        time_last_update_unix = 1633024805L,
        time_last_update_utc = "2021-09-30T14:00:05Z",
        time_next_update_unix = 1633111205L,
        time_next_update_utc = "2021-10-01T14:00:05Z",
        base_code = "CAD",
        conversion_rates = ConversionRates(USD = 0.80, AUD = 1.04, BGN = 1.32, CAD = 1.0, CHF = 0.92, CNY = 6.45, EGP = 15.67, EUR = 0.85, GBP = 0.73)
        )
       val currencyResponse6= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs6",
        terms_of_use = "https://example.com/terms6",
        time_last_update_unix = 1633024806L,
        time_last_update_utc = "2021-09-30T14:00:06Z",
        time_next_update_unix = 1633111206L,
        time_next_update_utc = "2021-10-01T14:00:06Z",
        base_code = "AUD",
        conversion_rates = ConversionRates(USD = 0.74, AUD = 1.0, BGN = 1.68, CAD = 0.93, CHF = 0.89, CNY = 6.35, EGP = 15.60, EUR = 0.81, GBP = 0.68)
        )
        val  currencyResponse7= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs7",
        terms_of_use = "https://example.com/terms7",
        time_last_update_unix = 1633024807L,
        time_last_update_utc = "2021-09-30T14:00:07Z",
        time_next_update_unix = 1633111207L,
        time_next_update_utc = "2021-10-01T14:00:07Z",
        base_code = "CNY",
        conversion_rates = ConversionRates(USD = 0.15, AUD = 1.09, BGN = 1.80, CAD = 1.05, CHF = 0.96, CNY = 1.0, EGP = 16.05, EUR = 0.86, GBP = 0.76)
        )
       val currencyResponse8= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs8",
        terms_of_use = "https://example.com/terms8",
        time_last_update_unix = 1633024808L,
        time_last_update_utc = "2021-09-30T14:00:08Z",
        time_next_update_unix = 1633111208L,
        time_next_update_utc = "2021-10-01T14:00:08Z",
        base_code = "CHF",
        conversion_rates = ConversionRates(USD = 1.09, AUD = 1.46, BGN = 1.96, CAD = 1.38, CHF = 1.0, CNY = 7.0, EGP = 18.0, EUR = 0.92, GBP = 0.82)
        )
       val currencyResponse9= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs9",
        terms_of_use = "https://example.com/terms9",
        time_last_update_unix = 1633024809L,
        time_last_update_utc = "2021-09-30T14:00:09Z",
        time_next_update_unix = 1633111209L,
        time_next_update_utc = "2021-10-01T14:00:09Z",
        base_code = "EGP",
        conversion_rates = ConversionRates(USD = 0.064, AUD = 0.084, BGN = 0.103, CAD = 0.091, CHF = 0.059, CNY = 0.416, EGP = 1.0, EUR = 0.054, GBP = 0.048)
        )
       val currencyResponse10= CurrencyResponse(
        result = "success",
        documentation = "https://example.com/docs10",
        terms_of_use = "https://example.com/terms10",
        time_last_update_unix = 1633024810L,
        time_last_update_utc = "2021-09-30T14:00:10Z",
        time_next_update_unix = 1633111210L,
        time_next_update_utc = "2021-10-01T14:00:10Z",
        base_code = "BGN",
        conversion_rates = ConversionRates(USD = 0.58, AUD = 0.73, BGN = 1.0, CAD = 0.79, CHF = 0.72, CNY = 4.08, EGP = 9.7, EUR = 0.51, GBP = 0.45)
        )

       val currencyResponseList = listOf( currencyResponse1,currencyResponse2,currencyResponse3,currencyResponse4,currencyResponse5,currencyResponse6,currencyResponse7,currencyResponse8,currencyResponse9,currencyResponse10)

    }
}