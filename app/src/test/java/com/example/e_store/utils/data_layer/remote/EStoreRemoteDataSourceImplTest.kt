package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.shared_models.*
import com.example.e_store.utils.test_utils.AddressMockModel
import com.example.e_store.utils.test_utils.BrandsMockModel
import com.example.e_store.utils.test_utils.ProductMockModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest


class EStoreRemoteDataSourceImplTest(

) : EStoreRemoteDataSource {

    var delayTime = 0
    var expectedProducts = ProductMockModel.products
    var emptyProducts = false


    override suspend fun fetchBrands(): Flow<List<Brand>> {
        val filteredBrands = BrandsMockModel.brands.distinctBy { it.title }
        return flowOf(filteredBrands)
    }

    override suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {
        return flowOf(ProductMockModel.collections[brandId] ?: emptyList())
    }

    override suspend fun fetchForUProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse?>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCustomCollections(): Flow<List<CustomCollection>> {
        TODO("Not yet implemented")
    }


    override suspend fun fetchCategoriesProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchProducts(): Flow<List<Product>> {
        if (emptyProducts) {
            return flowOf(emptyList())
        }
        return flowOf(expectedProducts)
    }

    override suspend fun fetchProduct(productId: Long): Flow<SingleProductResponse> {


        delay(delayTime.toLong())
        val product = expectedProducts.find { it.id == productId }
        return product?.let { flowOf(SingleProductResponse(it)) } ?: flow { }
    }


    override suspend fun createDraftOrder(shoppingCartDraftOrder: DraftOrderRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrder: DraftOrderRequest,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchDraftOrderByID(draftOrderId: Long): Flow<DraftOrderDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun removeDraftOrder(draftOrderId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchShoppingCartDraftOrders(email: String): Flow<DraftOrderDetails?> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllDraftOrders(): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllOrders(email: String): Flow<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDraftOrderToCompleteDraftOrder(draftOrderId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchProductById(productId: Long): SingleProductResponse {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllCustomers(): Flow<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(customer: CustomerRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomer(customerId: Long, customer: CustomerRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCustomerByEmail(email: String): Customer {
        TODO("Not yet implemented")
    }

    override suspend fun fetchDiscountCodesByCode(code: String): Flow<AppliedDiscount?> {
        TODO("Not yet implemented")
    }


    override suspend fun createCustomerAddress(customerId: Long, address: AddNewAddress) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCustomerAddress(customerId: Long, addressId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchDefaultAddress(customerId: Long): Flow<Address> {
        TODO("Not yet implemented")
    }


    override suspend fun updateCustomerAddress(
        customerId: Long,
        addressId: Long,
        address: AddNewAddress,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchConversionRates(): Flow<CurrencyResponse> {
        TODO("Not yet implemented")
    }

  /*  override suspend fun fetchCustomerAddresses(customerId: Long): Flow<AddressResponse> {
        return AddressMockModel.getAddressesByCustomerId(customerId).let {
            flowOf(AddressResponse(it))
        }
    }

    override suspend fun fetchCustomerAddress(
        customerId: Long,
        addressId: Long
    ): Flow<SingleAddressResponse> {
        try {
            fetchCustomerAddresses(customerId).collect(
                return AddressMockModel.getAddressById(addressId, customerId)?.let {
                    flowOf(SingleAddressResponse(it))
                } ?: throw Exception("Address not found")
            )
        } catch (e: Exception) {
            throw e
        }


    }
*/


    override suspend fun fetchCustomerAddresses(customerId: Long): Flow<AddressResponse> {
        return flow {
             delay(100)
            val customerAddresses = AddressMockModel.addresses.filter { it.id == customerId }
            emit(AddressResponse(customerAddresses))
        }
    }

    override suspend fun fetchCustomerAddress(customerId: Long, addressId: Long): Flow<SingleAddressResponse> {
        return flow {
            delay(100)
            val address = AddressMockModel.addresses.find { it.id == addressId && it.id == customerId }
            if (address != null) {
                emit(SingleAddressResponse(address))
            } else {
                throw Exception("Address not found")
            }
        }
    }

    override suspend fun getCountries(): Flow<List<CountryInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCitiesByCountry(country: String): Flow<List<GeoNameLocation>> {
        TODO("Not yet implemented")
    }
}
