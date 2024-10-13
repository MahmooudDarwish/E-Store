package com.example.e_store.utils.shared_models

data class CountryInfo(
    val continent: String,
    val capital: String,
    val languages: String,
    val geonameId: Int,
    val south: Double,
    val isoAlpha3: String,
    val north: Double,
    val fipsCode: String,
    val population: String,
    val east: Double,
    val isoNumeric: String,
    val areaInSqKm: String,
    val countryCode: String,
    val west: Double,
    val countryName: String,
    val postalCodeFormat: String,
    val continentName: String,
    val currencyCode: String
)

data class CountryInfoResponse(
    val geonames: List<CountryInfo>
)
