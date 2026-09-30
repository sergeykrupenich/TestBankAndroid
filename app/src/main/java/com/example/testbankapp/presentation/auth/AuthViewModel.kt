package com.example.testbankapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testbankapp.core.Resource
import com.example.testbankapp.domain.usecase.LoginUseCase
import com.example.testbankapp.domain.usecase.RegisterUseCase
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val login: LoginUseCase,
    private val register: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onTabSelected(tab: AuthTab) {
        _uiState.update {
            it.copy(
                selectedTab = tab,
                errorMessage = null,
                passwordInput = "",
                confirmPasswordInput = ""
            )
        }
    }

    fun onLoginChanged(value: String) {
        _uiState.update { it.copy(loginInput = value, errorMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(passwordInput = value, errorMessage = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPasswordInput = value, errorMessage = null) }
    }

    fun submit() {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = if (currentState.selectedTab == AuthTab.LOGIN) {
                login(currentState.loginInput, currentState.passwordInput)
            } else {
                register(
                    currentState.loginInput,
                    currentState.passwordInput,
                    currentState.confirmPasswordInput,
                    generateRandomData("Mobile first name "),
                    generateRandomData("Mobile last name "),
                )
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Unknown error"
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun generateRandomData(prefix: String): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val randomValue = (1..16)
            .map { allowedChars[Random.nextInt(allowedChars.length)] }
            .joinToString("")

        return "$prefix $randomValue"
    }
}

enum class AuthTab { LOGIN, REGISTER }

data class AuthUiState(
    val selectedTab: AuthTab = AuthTab.LOGIN,
    val loginInput: String = "",
    val passwordInput: String = "",
    val confirmPasswordInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
