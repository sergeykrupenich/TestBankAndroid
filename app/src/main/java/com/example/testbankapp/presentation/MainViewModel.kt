package com.example.testbankapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testbankapp.domain.usecase.InitSessionUseCase
import com.example.testbankapp.domain.usecase.IsUserLoggedInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val initEncryptedSession: InitSessionUseCase,
    private val isUserLoggedIn: IsUserLoggedInUseCase,
) : ViewModel() {

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    fun initSession() {
        viewModelScope.launch {
            initEncryptedSession()

            val isLoggedIn = isUserLoggedIn()
            _splashState.value = SplashState.Success(isLoggedIn)
        }
    }

    sealed interface SplashState {
        data object Loading : SplashState
        data class Success(val isLoggedIn: Boolean) : SplashState
    }
}
