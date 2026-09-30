package com.example.testbankapp.domain.usecase

import com.example.testbankapp.data.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
