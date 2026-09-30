package com.example.testbankapp.data.repository

import com.example.testbankapp.data.api.AccountApi
import com.example.testbankapp.data.dto.AccountResponse
import com.example.testbankapp.data.dto.CreateAccountRequest
import com.example.testbankapp.data.dto.DepositRequest
import com.example.testbankapp.data.dto.TransactionResponse
import com.example.testbankapp.core.Resource
import java.math.BigDecimal

class AccountRepositoryImpl(private val api: AccountApi) : AccountRepository {

    override suspend fun getAccounts(): Resource<List<AccountResponse>> {
        return try {
            val response = api.getAccounts()
            if (response.isSuccessful && response.body() != null) {
                val accounts = response.body()!!.map { dto ->
                    AccountResponse(
                        id = dto.id,
                        accountNumber = dto.accountNumber,
                        balance = dto.balance,
                        currency = dto.currency
                    )
                }
                Resource.Success(accounts)
            } else {
                Resource.Error(response.message() ?: "Failed to fetch accounts")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun createAccount(currency: String): Resource<AccountResponse> {
        return try {
            val response = api.createAccount(CreateAccountRequest(currency))
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Resource.Success(
                    AccountResponse(
                        id = dto.id,
                        accountNumber = dto.accountNumber,
                        balance = dto.balance,
                        currency = dto.currency,
                    )
                )
            } else {
                Resource.Error(response.message() ?: "Could not create account")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred")
        }
    }

    override suspend fun deposit(
        accountNumber: String,
        amount: BigDecimal,
        idempotencyKey: String
    ): Resource<TransactionResponse> {
        return try {
            val response = api.deposit(
                idempotencyKey = idempotencyKey,
                request = DepositRequest(accountNumber, amount)
            )
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Resource.Success(
                    TransactionResponse(
                        transactionId = dto.transactionId,
                        status = dto.status,
                        amount = dto.amount,
                        currency = dto.currency,
                        targetAccountNumber = accountNumber,
                    )
                )
            } else {
                Resource.Error(response.message() ?: "Deposit failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred")
        }
    }
}
