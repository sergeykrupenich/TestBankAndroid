package com.example.testbankapp.data.api

import com.example.testbankapp.data.dto.AuthRequest
import com.example.testbankapp.data.dto.AuthResponse
import com.example.testbankapp.data.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}
