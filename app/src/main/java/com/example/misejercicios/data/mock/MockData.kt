package com.example.misejercicios.data.mock

data class Product(
    val id: String,
    val name: String,
    val maskedNumber: String,
    val balance: Double,
    val currency: String,
    val type: ProductType
)

enum class ProductType { SAVINGS, CHECKING, CREDIT }

data class Transaction(
    val id: String,
    val description: String,
    val category: TransactionCategory,
    val date: String,
    val amount: Double,
    val currency: String
)

enum class TransactionCategory { SUPERMARKET, SALARY, ENTERTAINMENT, TRANSFER, RESTAURANT }

// Transferencias

enum class TransferType { BETWEEN_OWN, TO_BCP, INTERBANK, YAPE }

data class Account(
    val id: String,
    val label: String,
    val maskedNumber: String,
    val balance: Double,
    val currency: String
)

data class Beneficiary(
    val id: String,
    val name: String,
    val maskedAccount: String,
    val bank: String = "BCP"
)

// Tarjeta de credito

data class CreditCardDetail(
    val id: String,
    val holderName: String,
    val maskedNumber: String,
    val expiry: String,
    val network: String,
    val creditLine: Double,
    val totalDebt: Double,
    val minPayment: Double,
    val cutoffDate: String,
    val paymentDueDate: String,
    val currency: String,
    val cvv: String
)

data class CardConsumption(
    val id: String,
    val description: String,
    val date: String,
    val amount: Double,
    val installments: String? = null
)

// Perfil

data class UserProfile(
    val fullName: String,
    val email: String,
    val phone: String,
    val documentId: String,
    val initials: String
)

object MockData {
    const val USER_NAME = "Ivan"
    const val USER_INITIALS = "IV"
    const val ITF_RATE = 0.0015

    val products = listOf(
        Product("1", "Cuenta Ahorro", "****4821", 12_450.80, "Bs", ProductType.SAVINGS),
        Product("2", "Cuenta Corriente", "****3307", 2_890.50, "USD", ProductType.CHECKING),
        Product("3", "Tarjeta Visa", "****9934", -1_230.00, "USD", ProductType.CREDIT)
    )

    val transactions = listOf(
        Transaction("t1", "Supermercado Ketal", TransactionCategory.SUPERMARKET, "13 ago 2026", -320.50, "Bs"),
        Transaction("t2", "Sueldo Agosto", TransactionCategory.SALARY, "10 ago 2026", 5_500.00, "Bs"),
        Transaction("t3", "Netflix", TransactionCategory.ENTERTAINMENT, "08 ago 2026", -12.99, "USD"),
        Transaction("t4", "Transferencia enviada", TransactionCategory.TRANSFER, "05 ago 2026", -500.00, "Bs"),
        Transaction("t5", "Restaurante El Rancho", TransactionCategory.RESTAURANT, "02 ago 2026", -85.00, "Bs")
    )

    // Credenciales mocks de ejemplo para realizar el login
    const val VALID_USERNAME = "ivan.perez"
    const val VALID_PASSWORD = "Password123"

    val accounts = listOf(
        Account("acc1", "Cuenta Ahorro", "****4821", 12_450.80, "Bs"),
        Account("acc2", "Cuenta Corriente", "****3307", 2_890.50, "USD")
    )

    val frequentBeneficiaries = listOf(
        Beneficiary("b1", "Maria Fernandez", "****5521"),
        Beneficiary("b2", "Carlos Rojas", "****7789"),
        Beneficiary("b3", "Andrea Vargas", "****2234")
    )

    val creditCard = CreditCardDetail(
        id = "cc1",
        holderName = "IVAN PEREZ",
        maskedNumber = "****9934",
        expiry = "09/28",
        network = "VISA",
        creditLine = 15_000.00,
        totalDebt = 1_230.00,
        minPayment = 185.00,
        cutoffDate = "20 ago 2026",
        paymentDueDate = "05 sep 2026",
        currency = "USD",
        cvv = "482"
    )

    val cardConsumptions = listOf(
        CardConsumption("cc_1", "Amazon.com", "10 ago 2026", -89.90),
        CardConsumption("cc_2", "Farmacia Chavez", "08 ago 2026", -45.00),
        CardConsumption("cc_3", "Booking.com", "02 ago 2026", -320.00, "1/6"),
        CardConsumption("cc_4", "Spotify", "01 ago 2026", -9.99),
        CardConsumption("cc_5", "Restaurante Fridays", "28 jul 2026", -68.50)
    )

    val userProfile = UserProfile(
        fullName = "Ivan Perez Quiroga",
        email = "ivan.perez@bcp.com.bo",
        phone = "+591 700 12345",
        documentId = "CI 7452136 LP",
        initials = USER_INITIALS
    )
}
