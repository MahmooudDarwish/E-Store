package com.example.e_store.utils.test_utils

import com.example.e_store.utils.shared_models.Address

class AddressMockModel {

    companion object {

        val address1 = Address(
            id = 1L,
            address1 = "123 Elm St",
            city = "Springfield",
            province = "IL",
            phone = "123-456-7890",
            zip = "62701",
            last_name = "Doe",
            first_name = "John",
            country = "USA",
            default = true,
            customer_id = 1L
        )
        val address2 = Address(
            id = 2L,
            address1 = "456 Maple Ave",
            city = "Lincoln",
            province = "NE",
            phone = "098-765-4321",
            zip = "68508",
            last_name = "Smith",
            first_name = "Jane",
            country = "USA",
            default = true,
            customer_id = 2L
        )
        val address3 = Address(
            id = 3L,
            address1 = "789 Oak St",
            city = "Madison",
            province = "WI",
            phone = "321-654-9870",
            zip = "53703",
            last_name = "Johnson",
            first_name = "Emma",
            country = "USA",
            default = true,
            customer_id = 3L
        )
        val address4 = Address(
            id = 4L,
            address1 = "321 Birch Rd",
            city = "Austin",
            province = "TX",
            phone = "654-321-0987",
            zip = "73301",
            last_name = "Brown",
            first_name = "Oliver",
            country = "USA",
            default = true,
            customer_id = 4L
        )
        val address5 = Address(
            id = 5L,
            address1 = "654 Pine Blvd",
            city = "Los Angeles",
            province = "CA",
            phone = "987-654-3210",
            zip = "90001",
            last_name = "Taylor",
            first_name = "Sophia",
            country = "USA",
            default = true,
            customer_id = 5L
        )
        val address6 = Address(
            id = 6L,
            address1 = "987 Cedar Pl",
            city = "Seattle",
            province = "WA",
            phone = "123-321-1234",
            zip = "98101",
            last_name = "Lee",
            first_name = "William",
            country = "USA",
            default = true,
            customer_id = 6L
        )

        val address7 = Address(
            id = 7L,
            address1 = "135 Spruce St",
            city = "Denver",
            province = "CO",
            phone = "456-654-4567",
            zip = "80201",
            last_name = "White",
            first_name = "Isabella",
            country = "USA",
            default = false,
            customer_id = 1L
        )
        val address8 = Address(
            id = 8L,
            address1 = "246 Willow Ave",
            city = "Portland",
            province = "OR",
            phone = "789-987-7890",
            zip = "97201",
            last_name = "Harris",
            first_name = "Liam",
            country = "USA",
            default = false,
            customer_id = 2L
        )

        val address9 = Address(
            id = 9L,
            address1 = "369 Cherry Ln",
            city = "Miami",
            province = "FL",
            phone = "321-123-3210",
            zip = "33101",
            last_name = "Walker",
            first_name = "Ava",
            country = "USA",
            default = true,
            customer_id = 10L
        )

        val address10 = Address(
            id = 10L,
            address1 = "258 Fir Ct",
            city = "Boston",
            province = "MA",
            phone = "654-456-6543",
            zip = "02101",
            last_name = "Martin",
            first_name = "Ethan",
            country = "USA",
            default = true,
            customer_id = 8L
        )

        val addresses = mutableListOf(
            address1,
            address2,
            address3,
            address4,
            address5,
            address6,
            address7,
            address8,
            address9,
            address10
        )

        fun getAddressesByCustomerId(customerId: Long): List<Address> {
            return if (addresses.isEmpty()) {
                emptyList()
            } else if (customerId == -1L) {
                throw Exception("Customer not found")
            } else
                addresses.filter { it.customer_id == customerId }

        }


        fun getAddressById(id: Long, customerId: Long): Address? {
            if (addresses.isEmpty()) {
                return null
            }
            return addresses.find { it.id == id && it.customer_id == customerId }
        }

        fun addAddress(address: Address) {
            addresses.add(address)
        }

        fun updateAddress(id: Long, updatedAddress: Address) {
            val index = addresses.indexOfFirst { it.id == id }
            if (index != -1) {
                addresses[index] = updatedAddress
            } else {
                throw Exception("Address not found")
            }
        }

        fun deleteAddress(id: Long, customerId: Long) {
            val index = addresses.indexOfFirst { it.id == id && it.customer_id == customerId }
            if (index != -1) {
                addresses.removeAt(index)
            } else {
                throw Exception("Address not found")
            }
        }

    }
}