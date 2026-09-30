package com.example.testbankapp.domain.usecase

import com.example.testbankapp.data.repository.AccountRepository
import com.example.testbankapp.core.Resource
import com.example.testbankapp.core.map
import com.example.testbankapp.domain.model.TransactionResult
import java.math.BigDecimal
import java.util.UUID

class DepositFundsUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(accountNumber: String, amount: BigDecimal): Resource<TransactionResult> {
        if (amount <= BigDecimal.ZERO) {
            return Resource.Error("Amount must be greater than zero")
        }
        val idempotencyKey = UUID.randomUUID().toString()

        return repository.deposit(accountNumber, amount, idempotencyKey).map { dto ->
            TransactionResult(
                transactionId = dto.transactionId,
                status = dto.status.toString(),
                amount = dto.amount,
                currency = dto.currency,
            )
        }
    }
}
