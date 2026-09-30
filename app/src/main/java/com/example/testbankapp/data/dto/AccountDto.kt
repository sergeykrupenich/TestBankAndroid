package com.example.testbankapp.data.dto

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CreateAccountRequest(
    val currency: String
)

@Serializable
data class AccountResponse(
    val id: Long,
    val accountNumber: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,
    val currency: String,
    val createdAt: String? = null,
)

@Serializable
data class BalanceResponse(
    val accountNumber: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,
    val currency: String,
    val timestamp: String
)
