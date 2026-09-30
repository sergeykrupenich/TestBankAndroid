package com.example.testbankapp.data.repository

import com.example.testbankapp.data.dto.AuthRequest
import com.example.testbankapp.core.Resource
import com.example.testbankapp.data.dto.RegisterRequest

interface AuthRepository {
    suspend fun initSession()
    fun isUserLoggedIn(): Boolean
    suspend fun login(request: AuthRequest): Resource<Unit>
    suspend fun register(request: RegisterRequest): Resource<Unit>
    suspend fun logout()
}
