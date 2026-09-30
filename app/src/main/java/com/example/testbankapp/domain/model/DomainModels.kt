package com.example.testbankapp.domain.model

import java.math.BigDecimal

enum class Currency { BYN, USD, EUR, CYN }

enum class TransactionStatus { PENDING, SUCCESS, FAILED }

data class UserAuth(
    val token: String
)

data class Account(
    val id: Long,
    val accountNumber: String,
    val balance: BigDecimal,
    val currency: Currency,
    val createdAt: String? = null,
)

data class Transaction(
    val id: Long,
    val sourceAccount: String?,
    val targetAccount: String?,
    val amount: BigDecimal,
    val currency: Currency,
    val status: TransactionStatus,
    val timestamp: String
)
