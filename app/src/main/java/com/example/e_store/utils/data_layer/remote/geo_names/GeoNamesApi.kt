package com.example.e_store.utils.data_layer.remote.geo_names

import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.shared_models.CountryInfoResponse
import com.example.e_store.utils.shared_models.CurrencyResponse
import com.example.e_store.utils.shared_models.GeoNameLocationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GeoNamesApi {
    @GET(APIKeys.GEONAMES_COUNTRY_INFO_ENDPOINT)
    suspend fun getCountries(
        @Query(APIKeys.USERNAME_PARAM) username: String = APIKeys.USERNAME,
    ): CountryInfoResponse

    @GET(APIKeys.GEONAMES_SEARCH_ENDPOINT)
    suspend fun getCitiesByCountry(
        @Query(APIKeys.COUNTRY_PARAM) country: String,
        @Query(APIKeys.USERNAME_PARAM) username: String = APIKeys.USERNAME,
        @Query(APIKeys.FEATURE_CLASS_PARAM) featureClass: String = "P",
    ): GeoNameLocationResponse
}

