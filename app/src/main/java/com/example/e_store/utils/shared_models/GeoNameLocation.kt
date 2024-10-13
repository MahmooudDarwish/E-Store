package com.example.e_store.utils.shared_models

data class GeoNameLocation(
    val adminCode1: String,
    val lng: String,
    val geonameId: Int,
    val toponymName: String,
    val countryId: String,
    val fcl: String,
    val population: Int,
    val countryCode: String,
    val name: String,
    val fclName: String,
    val adminCodes1: AdminCodes1,
    val countryName: String,
    val fcodeName: String,
    val adminName1: String,
    val lat: String,
    val fcode: String
)

data class AdminCodes1(
    val ISO3166_2: String
)


data class GeoNameLocationResponse(
    val geonames: List<GeoNameLocation>
)