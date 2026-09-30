package com.example.testbankapp.domain.usecase

import com.example.testbankapp.data.repository.AuthRepository

class InitSessionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.initSession()
    }
}
