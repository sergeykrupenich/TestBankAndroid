package com.example.testbankapp.domain.usecase

import com.example.testbankapp.core.Resource
import com.example.testbankapp.core.map
import com.example.testbankapp.data.repository.AccountRepository
import com.example.testbankapp.domain.model.Account
import com.example.testbankapp.domain.model.Currency

class CreateAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(currency: String): Resource<Account> {
        if (currency.isBlank()) {
            return Resource.Error("Select a currency")
        }
        return accountRepository.createAccount(currency).map {  response ->
            Account(
                id = response.id,
                accountNumber = response.accountNumber,
                balance = response.balance,
                currency = Currency.valueOf(response.currency),
                createdAt = response.createdAt,
            )
        }
    }
}
