package com.example.testbankapp.data.dto

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class TransferRequest(
    val sourceAccountNumber: String,
    val targetAccountNumber: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal
)

@Serializable
data class DepositRequest(
    val accountNumber: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal
)

@Serializable
enum class TransactionStatus { PENDING, SUCCESS, FAILED }

@Serializable
data class TransactionResponse(
    val transactionId: Long,
    val sourceAccountNumber: String? = null,
    val targetAccountNumber: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val currency: String,
    val status: TransactionStatus,
    val timestamp: String? = null,
)
