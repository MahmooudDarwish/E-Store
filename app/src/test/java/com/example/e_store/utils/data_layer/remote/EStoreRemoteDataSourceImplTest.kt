package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.shared_models.*
import com.example.e_store.utils.test_utils.BrandsMockModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class EStoreRemoteDataSourceImplTest(

) : EStoreRemoteDataSource {

    override suspend fun fetchBrands(): Flow<List<Brand>> {
        val filteredBrands = BrandsMockModel.brands.distinctBy { it.title }
        return flowOf(filteredBrands)
    }

    override suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchForUProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchDiscountCodes(): Flow<DiscountCodesResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCustomCollections(): Flow<List<CustomCollection>> {
        TODO("Not yet implemented")
    }



    override suspend fun fetchCategoriesProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
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


}