package com.example.testbankapp.data.repository

import com.example.testbankapp.data.api.AuthApi
import com.example.testbankapp.data.dto.AuthRequest
import com.example.testbankapp.data.local.TokenManager
import com.example.testbankapp.core.Resource
import com.example.testbankapp.data.dto.RegisterRequest

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(request: AuthRequest): Resource<Unit> {
        return try {
            val response = authApi.login(request)
            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token
                tokenManager.saveToken(token)
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message() ?: "Auth failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    override suspend fun register(request: RegisterRequest): Resource<Unit> {
        return try {
            val response = authApi.register(request)
            if (response.isSuccessful && response.body() != null) {
                tokenManager.saveToken(response.body()!!.token)
                Resource.Success(Unit)
            } else {
                Resource.Error(response.message().ifEmpty { "Registration error" })
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network connection error")
        }
    }

    override suspend fun initSession() {
        tokenManager.initToken()
    }

    override fun isUserLoggedIn(): Boolean {
        return tokenManager.getToken() != null
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }
}
