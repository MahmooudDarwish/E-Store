package com.example.e_store.utils.data_layer

import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.AppliedDiscount
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.CountryInfo
import com.example.e_store.utils.shared_models.CurrencyResponse
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.shared_models.Customer
import com.example.e_store.utils.shared_models.CustomerRequest
import com.example.e_store.utils.shared_models.CustomerResponse
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.GeoNameLocation
import com.example.e_store.utils.shared_models.Order
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.SingleAddressResponse
import com.example.e_store.utils.shared_models.SingleProductResponse
import com.example.e_store.utils.test_utils.AddressMockModel
import com.example.e_store.utils.test_utils.BrandsMockModel
import com.example.e_store.utils.test_utils.ProductMockModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeEStoreRepositoryEmp : EStoreRepository {

    var shouldThrowError = false

    var listIsEmpty = false

    var expectedProducts = ProductMockModel.products


    override suspend fun fetchBrands(): Flow<List<Brand>> {
        val brandList = BrandsMockModel.brands
        return flowOf(brandList)
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

    override suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {
        return flowOf(ProductMockModel.collections[brandId] ?: emptyList())
    }

    override suspend fun fetchCategoriesProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchProducts(): Flow<List<Product>>  {
        if (shouldThrowError) {
            throw Exception("Error fetching products")
            } else if (listIsEmpty) {
            return flowOf(emptyList())
        } else {
            return flowOf(expectedProducts)
        }
    }


    override suspend fun fetchProduct(productId: Long): Flow<SingleProductResponse> {
        TODO("Not yet implemented")
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

    override suspend fun fetchCustomerAddresses(customerId: Long): Flow<AddressResponse> {
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



    override suspend fun getCountries(): Flow<List<CountryInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCitiesByCountry(country: String): Flow<List<GeoNameLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCustomerAddress(
        customerId: Long,
        addressId: Long
    ): Flow<SingleAddressResponse> {

        val address = AddressMockModel.getAddressById(addressId, customerId)

        if (address != null) {
            return flowOf(SingleAddressResponse(address))
        } else {
            throw Exception("Address not found")

        }
    }

}