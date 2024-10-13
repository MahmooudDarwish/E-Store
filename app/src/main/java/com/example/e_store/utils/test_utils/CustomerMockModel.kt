package com.example.e_store.utils.test_utils

import com.example.e_store.utils.shared_models.Customer

class CustomerMockModel {


        companion object {
           val customer1 = Customer(
            first_name = "John",
            last_name = "Doe",
            email = "john.doe@example.com",
            phone = "1234567890",
            id = 1L,
            verified_email = true,
            send_email_welcome = true,
            addresses =  listOf(
                AddressMockModel.address1,
                AddressMockModel.address7,


            )
            )
           val customer2 = Customer(
            first_name = "Jane",
            last_name = "Smith",
            email = "jane.smith@example.com",
            phone = "0987654321",
            id = 2L,
            verified_email = false,
            send_email_welcome = false,
            addresses =  listOf(
                AddressMockModel.address2,
                AddressMockModel.address8,

            )
            )
            val customer3 = Customer(
            first_name = "Mohamed",
            last_name = "Abdelrehim",
            email = "mohamed.abdelrehim@example.com",
            phone = "1231231234",
            id = 3L,
            verified_email = true,
            send_email_welcome = true,
             addresses =  listOf(
                AddressMockModel.address3,
            )
            )
           val customer4 = Customer(
            first_name = "Emma",
            last_name = "Johnson",
            email = "emma.johnson@example.com",
            phone = "3213213210",
            id = 4L,
            verified_email = true,
            send_email_welcome = true,
            addresses =  listOf(
                AddressMockModel.address4,
            )
            )
            val customer5 = Customer(
            first_name = "Oliver",
            last_name = "Brown",
            email = "oliver.brown@example.com",
            phone = "2223334444",
            id = 5L,
            verified_email = false,
            send_email_welcome = false,
             addresses =  listOf(
                AddressMockModel.address5,
            )
            )
           val customer6 = Customer(
            first_name = "Sophia",
            last_name = "Taylor",
            email = "sophia.taylor@example.com",
            phone = "5556667777",
            id = 6L,
            verified_email = true,
            send_email_welcome = true,
            addresses =  listOf(
                AddressMockModel.address6,
            )
            )
           val customer7 = Customer(
            first_name = "William",
            last_name = "Lee",
            email = "william.lee@example.com",
            phone = "8889990000",
            id = 7L,
            verified_email = false,
            send_email_welcome = false,

            )
           val customer8 = Customer(
            first_name = "Isabella",
            last_name = "White",
            email = "isabella.white@example.com",
            phone = "7778889999",
            id = 8L,
            verified_email = true,
            send_email_welcome = true,
            addresses =  listOf(
                AddressMockModel.address10,
            )
            )
           val customer9 = Customer(
            first_name = "Liam",
            last_name = "Harris",
            email = "liam.harris@example.com",
            phone = "9990001111",
            id = 9L,
            verified_email = false,
            send_email_welcome = false
            )
           val customer10 = Customer(
            first_name = "Ava",
            last_name = "Walker",
            email = "ava.walker@example.com",
            phone = "3334445555",
            id = 10L,
            verified_email = true,
            send_email_welcome = true,
            addresses =  listOf(
                AddressMockModel.address9,
            )
            )
        }

    val customers = mutableListOf( customer1, customer2, customer3, customer4, customer5, customer6, customer7, customer8, customer9, customer10)

    fun getCustomerById(id: Long): Customer? {
        return customers.find { it.id == id }
    }
    fun addCustomer(customer: Customer) {
        customers.add(customer)
    }

}

