package com.example.testbankapp.presentation.accounts

import androidx.lifecycle.ViewModel
import com.example.testbankapp.domain.usecase.CreateAccountUseCase
import com.example.testbankapp.domain.usecase.DepositFundsUseCase
import com.example.testbankapp.domain.usecase.GetAccountsUseCase
import androidx.lifecycle.viewModelScope
import com.example.testbankapp.core.Resource
import com.example.testbankapp.domain.model.Account
import com.example.testbankapp.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class AccountsViewModel(
    private val getAccounts: GetAccountsUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val depositFunds: DepositFundsUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    init {
        fetchAccounts()
    }

    fun fetchAccounts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getAccounts()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, accounts = result.data ?: emptyList())
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
                else -> {}
            }
        }
    }

    fun createAccount(currency: String = "USD") {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = createAccountUseCase(currency)) {
                is Resource.Success -> {
                    fetchAccounts()
                    _uiState.update { it.copy(successMessage = "Account Created!") }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                else -> {}
            }
        }
    }

    fun depositFunds(accountNumber: String, amount: String) {
        val parsedAmount = amount.toBigDecimalOrNull()
        if (parsedAmount == null || parsedAmount <= BigDecimal.ZERO) {
            _uiState.update { it.copy(errorMessage = "Enter a valid positive amount") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = depositFunds(accountNumber, parsedAmount)) {
                is Resource.Success -> {
                    fetchAccounts()
                    _uiState.update { it.copy(successMessage = "Deposit Successful!") }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                else -> {}
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    data class AccountsUiState(
        val isLoading: Boolean = false,
        val accounts: List<Account> = emptyList(),
        val errorMessage: String? = null,
        val successMessage: String? = null
    )
}
