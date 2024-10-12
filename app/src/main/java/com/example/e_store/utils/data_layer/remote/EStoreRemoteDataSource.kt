package com.example.e_store.utils.data_layer.remote

import com.example.e_store.utils.shared_models.AddNewAddress
import com.example.e_store.utils.shared_models.Address
import com.example.e_store.utils.shared_models.AddressResponse
import com.example.e_store.utils.shared_models.AppliedDiscount
import com.example.e_store.utils.shared_models.Brand
import com.example.e_store.utils.shared_models.CurrencyResponse
import kotlinx.coroutines.flow.Flow
import com.example.e_store.utils.shared_models.DiscountCodesResponse
import com.example.e_store.utils.shared_models.Product
import com.example.e_store.utils.shared_models.CustomCollection
import com.example.e_store.utils.shared_models.Customer
import com.example.e_store.utils.shared_models.CustomerRequest
import com.example.e_store.utils.shared_models.CustomerResponse
import com.example.e_store.utils.shared_models.DraftOrderDetails
import com.example.e_store.utils.shared_models.DraftOrderRequest
import com.example.e_store.utils.shared_models.DraftOrderResponse
import com.example.e_store.utils.shared_models.ProductResponse
import com.example.e_store.utils.shared_models.SingleAddressResponse
import com.example.e_store.utils.shared_models.Order
import com.example.e_store.utils.shared_models.SingleProductResponse


interface EStoreRemoteDataSource {
    suspend fun fetchBrands(): Flow<List<Brand>>
    suspend fun fetchForUProducts(): Flow<List<Product>>
    suspend fun fetchDiscountCodes(): Flow<List<DiscountCodesResponse?>>
    suspend fun fetchCustomCollections(): Flow<List<CustomCollection>>
    suspend fun fetchBrandProducts(brandId: String): Flow<List<Product>>
    suspend fun fetchCategoriesProducts(): Flow<List<Product>>

    //fetch all products
    suspend fun fetchProducts(): Flow<List<Product>>

    suspend fun fetchProduct(productId: Long): Flow<SingleProductResponse>



    //shop cart draft order
    suspend fun createDraftOrder(shoppingCartDraftOrder: DraftOrderRequest)

    suspend fun updateDraftOrder(
        draftOrderId: Long,
        shoppingCartDraftOrder: DraftOrderRequest
    )

    suspend fun fetchDraftOrderByID(draftOrderId: Long): Flow<DraftOrderDetails>

    suspend fun removeDraftOrder(draftOrderId: Long)

    suspend fun fetchShoppingCartDraftOrders(
        email: String
    ): Flow<DraftOrderDetails?>

    suspend fun fetchAllDraftOrders(
    ): Flow<DraftOrderResponse>

    suspend fun fetchAllOrders(email: String): Flow<List<Order>>

    suspend fun updateDraftOrderToCompleteDraftOrder(draftOrderId: Long)


    suspend fun fetchProductById(productId: Long): SingleProductResponse



    suspend fun fetchAllCustomers(): Flow<CustomerResponse>
    suspend fun createCustomer(customer: CustomerRequest)


    suspend fun updateCustomer(customerId: Long, customer: CustomerRequest)
    suspend fun fetchCustomerByEmail(email: String): Customer

    suspend fun fetchDiscountCodesByCode(code: String): Flow<AppliedDiscount?>



    suspend fun fetchCustomerAddresses(customerId: Long): Flow<AddressResponse>
    suspend fun createCustomerAddress(customerId: Long, address: AddNewAddress)
    suspend fun deleteCustomerAddress(customerId: Long, addressId: Long)

    suspend fun fetchDefaultAddress(customerId: Long): Flow<Address?>
    suspend fun updateCustomerAddress(customerId: Long, addressId: Long, address: AddNewAddress)
    suspend fun fetchConversionRates(): Flow<CurrencyResponse>
    suspend fun fetchCustomerAddress(
        customerId: Long, addressId: Long
    ): Flow<SingleAddressResponse>
}


