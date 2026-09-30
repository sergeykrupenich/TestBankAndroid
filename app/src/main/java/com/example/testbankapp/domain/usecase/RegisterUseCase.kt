package com.example.testbankapp.domain.usecase

import com.example.testbankapp.core.Resource
import com.example.testbankapp.data.dto.RegisterRequest
import com.example.testbankapp.data.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        pass: String,
        confirmPass: String,
        firstName: String,
        lastName: String,
    ): Resource<Unit> {
        if (login.isBlank() || pass.isBlank()) {
            return Resource.Error("All fields must be filled")
        }
        if (pass != confirmPass) {
            return Resource.Error("Passwords didn't match")
        }
        if (pass.length < 6) {
            return Resource.Error("Password must contain at least 6 symbols")
        }
        return authRepository.register(
            RegisterRequest(login, pass, firstName, lastName)
        )
    }
}
