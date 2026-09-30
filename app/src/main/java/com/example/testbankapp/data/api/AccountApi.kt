package com.example.testbankapp.data.api

import com.example.testbankapp.data.dto.AccountResponse
import com.example.testbankapp.data.dto.CreateAccountRequest
import com.example.testbankapp.data.dto.DepositRequest
import com.example.testbankapp.data.dto.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApi {
    @GET("accounts")
    suspend fun getAccounts(): Response<List<AccountResponse>>

    @POST("accounts")
    suspend fun createAccount(@Body request: CreateAccountRequest): Response<AccountResponse>

    @POST("accounts/deposit")
    suspend fun deposit(
        @Header("X-Idempotency-Key") idempotencyKey: String,
        @Body request: DepositRequest
    ): Response<TransactionResponse>
}
