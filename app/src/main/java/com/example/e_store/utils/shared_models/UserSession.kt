package com.example.e_store.utils.shared_models

object UserSession {
    var email: String? = null
    var name: String? = null
    var phone: String? = null
    var Uid: String? = null
    var isGuest: Boolean = true
    var shopifyCustomerID: Long?=null
    var currency: String = "USD"
    var conversionRates: ConversionRates? = null


}