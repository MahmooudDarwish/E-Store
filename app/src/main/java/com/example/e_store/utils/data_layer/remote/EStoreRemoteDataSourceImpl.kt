package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.data_layer.remote.shopify.ShopifyAPIServices
import com.example.e_store.utils.data_layer.remote.shopify.ShopifyRetrofitHelper
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import android.util.Log
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import kotlinx.coroutines.flow.flow
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.constants.APIKeys
import com.example.e_store.utils.data_layer.remote.exchange_rate.ExchangeRateApi
import com.example.e_store.utils.data_layer.remote.exchange_rate.ExchangeRateRetrofitHelper
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.AppliedDiscount
import com.example.e_store.utils.shared_models.CurrencyResponse
import com.example.e_store.utils.shared_models.Customer
import com.example.e_store.utils.shared_models.CustomerRequest
import com.example.e_store.utils.shared_models.CustomerResponse
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.SingleAddressResponse
import com.example.e_store.utils.shared_models.Order
import com.example.e_store.utils.shared_models.SingleProductResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull

class EStoreRemoteDataSourceImpl private constructor() : EStoreRemoteDataSource {

    private val apiService: ShopifyAPIServices = ShopifyRetrofitHelper.api

    private val exchangeRateApiService: ExchangeRateApi =
        ExchangeRateRetrofitHelper.getInstance().create(ExchangeRateApi::class.java)

    private val TAG = "EShopRemoteDataSourceImpl"

    companion object {
        @Volatile
        private var instance: EStoreRemoteDataSourceImpl? = null

        fun getInstance(): EStoreRemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                instance ?: EStoreRemoteDataSourceImpl().also { instance = it }
            }
        }
    }

    override suspend fun fetchBrands(): Flow<List<Brand>> {
        val response = apiService.fetchBrands().smart_collections

        val filteredBrands = response.distinctBy { it.title }

        return flowOf(filteredBrands)
    }

    override suspend fun fetchProducts(): Flow<List<Product>> {
        val response = apiService.fetchProducts().products
        return flowOf(response)
    }

    override suspend fun fetchForUProducts(): Flow<List<Product>> {
        return try {
            val customCollections = fetchCustomCollections()

            var homeCollection: CustomCollection? = null

            customCollections.collect { response ->
                homeCollection = response.firstOrNull { collection ->
                    collection.title.contains(APIKeys.HOME, ignoreCase = true)
                }
            }
            homeCollection?.let {
                val response =
                    apiService.fetchProducts(limit = 4, collectionId = it.id.toString()).products
                flowOf(response)
            } ?: flowOf(emptyList())

        } catch (e: Exception) {
            Log.d(TAG, "customCollections error: ${e.message}")
            flowOf(emptyList())
        }
    }

    override suspend fun fetchCategoriesProducts(): Flow<List<Product>> {
        return try {
            val customCollections = fetchCustomCollections()
            val allProducts = mutableListOf<Product>()
            val productIdsSet = mutableSetOf<Long>()

            customCollections.collect { response ->
                val filteredCollections = response.filter { collection ->
                    !collection.title.equals(APIKeys.HOME, ignoreCase = true)
                }

                for (collection in filteredCollections) {
                    val products =
                        apiService.fetchProducts(collectionId = collection.id.toString()).products

                    val uniqueProducts = products.filter { product ->
                        product.id !in productIdsSet
                    }

                    uniqueProducts.forEach { product ->
                        productIdsSet.add(product.id)
                        allProducts.add(product)
                    }
                }
            }

            flowOf(allProducts)

        } catch (e: Exception) {
            Log.d(TAG, "customCollections error: ${e.message}")
            flowOf(emptyList())
        }
    }


    override suspend fun fetchCustomCollections(): Flow<List<CustomCollection>> {
        val response = apiService.fetchCustomCollections().customCollections
        return flowOf(response)
    }

    override suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {
        val response = apiService.fetchProducts(collectionId = brandId).products
        return flowOf(response)
    }


    override suspend fun createDraftOrder(shoppingCartDraftOrder: DraftOrderRequest) {
        Log.d(TAG, "createShoppingCartDraftOrder: $shoppingCartDraftOrder")
        apiService.createDraftOrder(shoppingCartDraftOrder)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrder: DraftOrderRequest
    ) {
        apiService.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
    }

    override suspend fun removeDraftOrder(draftOrderId: Long) {
        apiService.removeDraftDraftOrder(draftOrderId)
    }

    override suspend fun fetchDraftOrderByID(draftOrderId: Long): Flow<DraftOrderDetails> {
        return flow {
            val response = apiService.fetchDraftOrderByID(draftOrderId).draft_order
            emit(response)
        }
    }

    override suspend fun fetchAllDraftOrders(): Flow<DraftOrderResponse> {
        return flow {
            val response = apiService.fetchAllDraftOrders()
            emit(response)
        }
    }


    override suspend fun fetchShoppingCartDraftOrders(email: String): Flow<DraftOrderDetails?> {
        val response = apiService.fetchAllDraftOrders().draft_orders

        val filteredOrders = response.filter {
            val emailFilter = it.email == email
            val draftOrderType = it.note == APIKeys.SHOPPING_CART
            emailFilter && draftOrderType
        }
        return if (filteredOrders.isNotEmpty()) {
            flowOf(filteredOrders[0])

        } else {
            flowOf(null)
        }
    }


    override suspend fun fetchProduct(productId: Long): Flow<SingleProductResponse> {

        return flowOf(
            apiService.fetchProduct(productId = productId)
        )
    }


    override suspend fun fetchAllOrders(email: String): Flow<List<Order>> {

        val response = apiService.fetchOrders().orders

        val filteredOrders = response.filter {
            it.email == email
        }
        return flowOf(filteredOrders)
    }

    override suspend fun updateDraftOrderToCompleteDraftOrder(draftOrderId: Long) {
        apiService.updateDraftOrderToCompleteDraftOrder(draftOrderId = draftOrderId)
    }

    override suspend fun fetchProductById(productId: Long): SingleProductResponse {

        return apiService.fetchProduct(productId)

    }

    override suspend fun fetchConversionRates(): Flow<CurrencyResponse> {
        return flow {
            val response = exchangeRateApiService.getLatestExchangeRates()
            emit(response)
        }
    }

    override suspend fun fetchCustomerAddress(customerId: Long, addressId: Long): Flow<SingleAddressResponse> {
        return flowOf(apiService.fetchCustomerAddress(customerId, addressId))
    }


    override suspend fun createCustomer(customer: CustomerRequest) {
        apiService.createCustomer(customer)
    }


    override suspend fun updateCustomer(customerId: Long, customer: CustomerRequest) {
        apiService.updateCustomer(customerId, customer)
    }


    override suspend fun fetchAllCustomers(): Flow<CustomerResponse> {
        return flow {
            val response = apiService.fetchAllCustomers()
            emit(response)
        }
    }

    override suspend fun fetchCustomerByEmail(email: String): Customer {
        Log.d("fetchCustomerByEmail", "fetchCustomerByEmail: $email")
        val response = apiService.fetchAllCustomers().customers.first { it.email == email }
        Log.d("fetchCustomerByEmail", "fetchCustomerByEmail: $response")
        return response
    }


    override suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse>> {
        return flow {
            try {
                val priceRuleResponse = apiService.fetchPricingRules()

                if (priceRuleResponse.isSuccessful) {
                    priceRuleResponse.body()?.let { body ->
                        val discountCodesList = mutableListOf<DiscountCodesResponse>()

                        for (priceRule in body.price_rules) {
                            Log.d(TAG, "priceRuleID: ${priceRule.id}")

                            // Fetch discount codes for each price rule
                            val discountCodeResponse = apiService.fetchDiscountCodes(priceRule.id)

                            // Add the discount code response to the list
                            discountCodeResponse?.let { discountCodesList.add(it) }
                        }

                        // Emit the complete list of discount codes after the loop
                        emit(discountCodesList)
                    } ?: run {
                        Log.e(TAG, "No price rules found.")
                        emit(emptyList()) // Emit an empty list if no price rules are found
                    }
                } else {
                    Log.e(TAG, "Failed to fetch pricing rules: ${priceRuleResponse.errorBody()?.string()}")
                    emit(emptyList()) // Emit an empty list in case of failure
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching discount codes: ${e.message}")
                emit(emptyList()) // Emit an empty list in case of an exception
            }
        }
    }
    override suspend fun fetchDiscountCodesByCode(code: String): Flow<AppliedDiscount?> = flow {
        fetchDiscountCodes().collect { discountResponses ->
            // Check if discountResponses is not empty
            if (discountResponses.isNotEmpty()) {
                // Find the discount code that matches the input code
                val discountCode = discountResponses.flatMap { it.discount_codes } // Flattening the list of discount codes
                    .find { it.code == code }

                if (discountCode != null) {
                    // Fetch price rule details based on the discount code's price rule ID
                    val priceRuleDetails = discountCode.price_rule_id?.let {
                        apiService.fetchPricingRuleByID(it)
                    }

                    // If price rule details are successfully fetched
                    if (priceRuleDetails != null) {
                        // Create an AppliedDiscount object with the fetched data
                        val appliedDiscount = AppliedDiscount(
                            description = priceRuleDetails.price_rule.title,
                            value = (priceRuleDetails.price_rule.value.toDouble() * -1).toString(),
                            title = priceRuleDetails.price_rule.title,
                            value_type = priceRuleDetails.price_rule.value_type,
                        )

                        emit(appliedDiscount)  // Emit the applied discount
                    } else {
                        emit(null) // Emit null if price rule details are not found
                    }
                } else {
                    emit(null) // Emit null if no matching discount code is found
                }
            } else {
                emit(null) // Emit null if no discount responses are found
            }
        }
    }

    /* override suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse>?> {
            return flow {
                try {
                    val priceRuleResponse = apiService.fetchPricingRules()

                    if (priceRuleResponse.isSuccessful) {
                        priceRuleResponse.body()?.let { body ->
                            for (priceRule in body.price_rules) {
                                Log.d(TAG, "priceRuleID: ${priceRule.id}")

                                // Fetch discount codes for each price rule
                                val discountCodeResponse = apiService.fetchDiscountCodes(priceRule.id)
                            }
                            // Emit the discount code response
                            emit(discountCodeResponse)

                        } ?: run {
                            Log.e(TAG, "No price rules found.")
                            emit(null) // Emit null if no price rules are found
                        }
                    } else {
                        Log.e(TAG, "Failed to fetch pricing rules: ${priceRuleResponse.errorBody()?.string()}")
                        emit(null) // Emit null in case of failure
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error fetching discount codes: ${e.message}")
                    emit(null) // Emit null in case of an exception
                }
            }
        }*/

/*
    override suspend fun fetchDiscountCodesByCode(code: String): Flow<AppliedDiscount?> = flow {
        fetchDiscountCodes().collect { discountResponse ->
            if (discountResponse != null) {
                val discountCode = discountResponse.discount_codes.find {
                    Log.d("fetchDiscountCodesByCode", "code: ${it.code}")
                    Log.d("fetchDiscountCodesByCode", "code: $code")
                    it.code == code

                }

                if (discountCode != null) {
                    val priceRuleDetails = discountCode.price_rule_id?.let {
                        apiService.fetchPricingRuleByID(it)
                    }

                    if (priceRuleDetails != null) {
                        // Create an AppliedDiscount object with the fetched data
                        val appliedDiscount = AppliedDiscount(
                            description = priceRuleDetails.price_rule.title,
                            value = (priceRuleDetails.price_rule.value.toDouble() * -1).toString(),
                            title = priceRuleDetails.price_rule.title,
                            value_type = priceRuleDetails.price_rule.value_type,
                        )


                        emit(appliedDiscount)  // Emit the applied discount
                    }
                } else {

                    emit(null)  // Emit null if no discount code is found
                }
            }
        }
    }*/



    /*
    * return flow {
            try {
                // Collect the discount codes flow
                fetchDiscountCodes().collect { discountCodesList ->

                    Log.d("testtttt", "Discount Codes List: ${discountCodesList}")

                    var priceID:Long?
                    // Check if the list is not null or empty
                    if (!discountCodesList.isNullOrEmpty()) {
                        for (discountCode in discountCodesList) {
                            Log.d("testtttt1", "Discount Code: ${discountCode}")

                             priceID = discountCode.discount_codes.firstOrNull { it.code == code }

                        }
                        // Find the discount code that matches the given code

                        Log.d("testtttt2", "Coupon Code: ${discountCodeResponse}")

                        if (priceID != null) {
                            // Fetch pricing rule details by price rule ID
                            val priceRuleDetails = apiService.fetchPricingRuleByID(priceID)

                            Log.d("testtttt3", "Pricing Rule Details: ${priceRuleDetails}")

                            if (priceRuleDetails != null) {
                                // Create an AppliedDiscount object with the fetched data
                                val appliedDiscount = AppliedDiscount(
                                    description = priceRuleDetails.price_rule.title,
                                    value = priceRuleDetails.price_rule.value,
                                    title = priceRuleDetails.price_rule.title,
                                    amount = priceRuleDetails.price_rule.value,
                                    valueType = priceRuleDetails.price_rule.value_type
                                )

                                Log.d("testtttt4", "Applied Discount: $appliedDiscount")

                                // Emit the applied discount
                                emit(appliedDiscount)
                                return@collect  // Exit the collection after finding the match
                            }
                        }
                    }
                    // Emit null if no matching discount code was found
                    emit(null)
                }
            } catch (e: Exception) {
                Log.e("fetchDiscountCodesByCode", "Error: ${e.message}")
                emit(null) // Emit null in case of an error
            }
        }
    * */
    override suspend fun fetchCustomerAddresses(customerId: Long): Flow<AddressResponse> {
        return flow {

            emit(apiService.fetchCustomerAddresses(customerId))
        }
    }

    override suspend fun createCustomerAddress(customerId: Long, address: AddNewAddress) {
                apiService.createCustomerAddress(customerId, address)

    }

    override suspend fun deleteCustomerAddress(customerId: Long, addressId: Long) {
        apiService.deleteCustomerAddress(customerId, addressId)
    }

    override suspend fun fetchDefaultAddress(customerId: Long): Flow<Address> {

           var defaultAddress :Address?= null

                fetchCustomerAddresses(customerId).collect { addresses ->
                    defaultAddress= addresses.addresses.first {
                        it.default == true
                    }
                }
           return flow {
               defaultAddress?.let { emit(it) }
           }
        }

    override suspend fun updateCustomerAddress(
        customerId: Long,
        addressId: Long,
        address: AddNewAddress
    ) {
        apiService.updateCustomerAddress(customerId, addressId, address)
    }



}

