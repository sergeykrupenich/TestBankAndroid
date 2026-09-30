package com.example.testbankapp.domain.model

import java.math.BigDecimal

data class TransactionResult(
    val transactionId: Long,
    val status: String,
    val amount: BigDecimal,
    val currency: String
)
