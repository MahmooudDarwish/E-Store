package com.example.e_store.utils.shared_methods

import com.example.e_store.utils.constants.CurrencyKeys
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.ProductDetails
import com.example.e_store.utils.shared_models.UserSession
import java.text.SimpleDateFormat
import java.util.Locale

fun initializeProductDetails(product: Product) {
    ProductDetails.id = product.id
    ProductDetails.title = product.title
    ProductDetails.vendor = product.vendor
    ProductDetails.price = convertCurrency(product.variants[0].price.toDouble())
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

fun convertCurrency(price: Double): String {

    val rates = UserSession.conversionRates
    val newCurrency = UserSession.currency
    val oldCurrency = CurrencyKeys.EGP

    if (oldCurrency == newCurrency) {
        return String.format("%.2f %s", price, newCurrency)
    }

    val currencyToRate = mapOf(
        CurrencyKeys.USD to rates?.USD,
        CurrencyKeys.AUD to rates?.AUD,
        CurrencyKeys.BGN to rates?.BGN,
        CurrencyKeys.CAD to rates?.CAD,
        CurrencyKeys.CHF to rates?.CHF,
        CurrencyKeys.CNY to rates?.CNY,
        CurrencyKeys.EGP to rates?.EGP,
        CurrencyKeys.EUR to rates?.EUR,
        CurrencyKeys.GBP to rates?.GBP
    )

    val oldCurrencyRate = currencyToRate[oldCurrency] ?: error("Invalid old currency")
    val newCurrencyRate = currencyToRate[newCurrency] ?: error("Invalid new currency")

    val priceInUSD = price / oldCurrencyRate
    val convertedPrice = priceInUSD * newCurrencyRate

    val convertedPriceString = String.format("%.2f", convertedPrice)

    return "$convertedPriceString $newCurrency"
}
