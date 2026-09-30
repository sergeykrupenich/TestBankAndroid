package com.example.testbankapp.domain.usecase

import com.example.testbankapp.core.Resource
import com.example.testbankapp.core.map
import com.example.testbankapp.data.repository.AccountRepository
import com.example.testbankapp.domain.model.Account
import com.example.testbankapp.domain.model.Currency

class GetAccountsUseCase(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Resource<List<Account>> {
        return accountRepository.getAccounts().map {
            it.map {  response ->
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
}
