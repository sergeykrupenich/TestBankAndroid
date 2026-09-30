package com.example.testbankapp.data.repository

import com.example.testbankapp.data.dto.AccountResponse
import com.example.testbankapp.data.dto.TransactionResponse
import com.example.testbankapp.core.Resource
import java.math.BigDecimal

interface AccountRepository {
    suspend fun getAccounts(): Resource<List<AccountResponse>>
    suspend fun createAccount(currency: String): Resource<AccountResponse>
    suspend fun deposit(
        accountNumber: String,
        amount: BigDecimal,
        idempotencyKey: String
    ): Resource<TransactionResponse>
}
