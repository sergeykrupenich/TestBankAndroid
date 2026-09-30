package com.example.testbankapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.testbankapp.presentation.accounts.AccountListScreen
import com.example.testbankapp.presentation.auth.LoginScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            mainViewModel.initSession()
        }

        enableEdgeToEdge()
        setContent {
            val splashState by mainViewModel.splashState.collectAsState()

            MaterialTheme {
                when (val state = splashState) {
                    is MainViewModel.SplashState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is MainViewModel.SplashState.Success -> {
                        if (state.isLoggedIn) {
                            AccountListScreen(
                                onLogoutClick = { mainViewModel.initSession() }
                            )
                        } else {
                            LoginScreen(
                                onAuthSuccess = { mainViewModel.initSession() }
                            )
                        }
                    }
                }
            }
        }
    }
}
