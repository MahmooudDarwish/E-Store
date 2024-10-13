package com.example.e_store.utils.data_layer

import android.util.Log
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSource
import com.example.e_store.utils.shared_models.Brand
import kotlinx.coroutines.flow.Flow
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.AppliedDiscount
import com.example.e_store.utils.shared_models.CountryInfo
import com.example.e_store.utils.shared_models.CurrencyResponse
import com.example.e_store.utils.shared_models.CustomerRequest
import com.example.e_store.utils.shared_models.CustomerResponse
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.GeoNameLocation
import com.example.e_store.utils.shared_models.GeoNameLocationResponse
import com.example.e_store.utils.shared_models.SingleAddressResponse
import com.example.e_store.utils.shared_models.Order
import com.example.e_store.utils.shared_models.SingleProductResponse

class EStoreRepositoryImpl private constructor(
    private var eStoreRemoteDataSource: EStoreRemoteDataSource,
) : EStoreRepository {

    private val TAG = "EShopRepositoryImpl"

    companion object {
        private var instance: EStoreRepositoryImpl? = null
        fun getInstance(
            moviesRemoteDataSource: EStoreRemoteDataSource,
        ): EStoreRepositoryImpl {
            return instance ?: synchronized(this) {
                val temp = EStoreRepositoryImpl(
                    moviesRemoteDataSource
                )
                instance = temp
                temp
            }

        }
    }

    override suspend fun fetchBrands(): Flow<List<Brand>> {
        return eStoreRemoteDataSource.fetchBrands()
    }

    override suspend fun fetchCustomCollections(): Flow<List<CustomCollection>> {
        return eStoreRemoteDataSource.fetchCustomCollections()
    }

    override suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchBrandProducts(brandId)
    }

    override suspend fun fetchCategoriesProducts(): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchCategoriesProducts()
    }

    override suspend fun fetchForUProducts(): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchForUProducts()
    }

    override suspend fun fetchProducts(): Flow<List<Product>> {
        return eStoreRemoteDataSource.fetchProducts()
    }

    override suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse?>> {
        return eStoreRemoteDataSource.fetchDiscountCodes()
    }

    override suspend fun createDraftOrder(shoppingCartDraftOrder: DraftOrderRequest) {
        Log.d(TAG, "createShoppingCartDraftOrder: $shoppingCartDraftOrder")
        eStoreRemoteDataSource.createDraftOrder(shoppingCartDraftOrder)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrder: DraftOrderRequest,
    ) {
        eStoreRemoteDataSource.updateDraftOrder(draftOrderId, shoppingCartDraftOrder)
    }

    override suspend fun fetchDraftOrderByID(draftOrderId: Long): Flow<DraftOrderDetails> {
        return eStoreRemoteDataSource.fetchDraftOrderByID(draftOrderId)
    }

    override suspend fun removeDraftOrder(draftOrderId: Long) {
        eStoreRemoteDataSource.removeDraftOrder(draftOrderId)
    }

    override suspend fun fetchShoppingCartDraftOrders(email: String): Flow<DraftOrderDetails?> {
        return eStoreRemoteDataSource.fetchShoppingCartDraftOrders(email = email)
    }

    override suspend fun fetchAllDraftOrders(): Flow<DraftOrderResponse> {
        return eStoreRemoteDataSource.fetchAllDraftOrders()
    }

    override suspend fun fetchAllOrders(email: String): Flow<List<Order>> {
        return eStoreRemoteDataSource.fetchAllOrders(email = email)
    }

    override suspend fun updateDraftOrderToCompleteDraftOrder(draftOrderId: Long) {
        eStoreRemoteDataSource.updateDraftOrderToCompleteDraftOrder(draftOrderId = draftOrderId)
    }

    override suspend fun fetchProduct(productId: Long): Flow<SingleProductResponse> {
        return eStoreRemoteDataSource.fetchProduct(productId)

    }

    override suspend fun fetchProductById(productId: Long): SingleProductResponse {
        return eStoreRemoteDataSource.fetchProductById(productId)

    }

    override suspend fun createCustomer(customer: CustomerRequest) {
        eStoreRemoteDataSource.createCustomer(customer)

    }

    override suspend fun updateCustomer(customerId: Long, customer: CustomerRequest) {
        eStoreRemoteDataSource.updateCustomer(customerId, customer)
    }

    override suspend fun fetchAllCustomers(): Flow<CustomerResponse> {
        return eStoreRemoteDataSource.fetchAllCustomers()
    }

    override suspend fun fetchCustomerByEmail(email: String): com.example.e_store.utils.shared_models.Customer {
        return eStoreRemoteDataSource.fetchCustomerByEmail(email)
    }

    override suspend fun fetchDiscountCodesByCode(code: String): Flow<AppliedDiscount?> {
        return eStoreRemoteDataSource.fetchDiscountCodesByCode(code)

    }

    override suspend fun fetchCustomerAddresses(customerId: Long): Flow<AddressResponse> {
        return eStoreRemoteDataSource.fetchCustomerAddresses(customerId)
    }

    override suspend fun createCustomerAddress(customerId: Long, address: AddNewAddress) {
        Log.d("EStoreRepositoryImpl", "createCustomerAddress: $address")
        eStoreRemoteDataSource.createCustomerAddress(customerId, address)

    }

    override suspend fun deleteCustomerAddress(customerId: Long, addressId: Long) {
        eStoreRemoteDataSource.deleteCustomerAddress(customerId, addressId)
    }

    override suspend fun fetchDefaultAddress(customerId: Long): Flow<Address?> {
        return eStoreRemoteDataSource.fetchDefaultAddress(customerId)
    }

    override suspend fun updateCustomerAddress(
        customerId: Long,
        addressId: Long,
        address: AddNewAddress,
    ) {
        eStoreRemoteDataSource.updateCustomerAddress(customerId, addressId, address)
    }

    override suspend fun fetchConversionRates(): Flow<CurrencyResponse> {
        return eStoreRemoteDataSource.fetchConversionRates()
    }

    override suspend fun fetchCustomerAddress(
        customerId: Long,
        addressId: Long,
    ): Flow<SingleAddressResponse> {
        return eStoreRemoteDataSource.fetchCustomerAddress(customerId, addressId)

    }

    override suspend fun getCountries(): Flow<List<CountryInfo>> {
        return eStoreRemoteDataSource.getCountries()
    }

    override suspend fun getCitiesByCountry(country: String): Flow<List<GeoNameLocation>> {
        return eStoreRemoteDataSource.getCitiesByCountry(country)
    }


}