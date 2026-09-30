package com.example.testbankapp.domain.usecase

import com.example.testbankapp.data.repository.AuthRepository

class IsUserLoggedInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}
