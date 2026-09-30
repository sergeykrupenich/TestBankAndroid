package com.example.testbankapp.domain.usecase

import com.example.testbankapp.core.Resource
import com.example.testbankapp.data.dto.AuthRequest
import com.example.testbankapp.data.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, pass: String): Resource<Unit> {
        if (login.isBlank() || pass.isBlank()) {
            return Resource.Error("Login or password cannot be empty")
        }
        return authRepository.login(AuthRequest(login, pass))
    }
}
