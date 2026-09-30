package com.example.testbankapp.di

import com.example.testbankapp.data.api.AccountApi
import com.example.testbankapp.data.api.AuthApi
import com.example.testbankapp.data.api.AuthInterceptor
import com.example.testbankapp.data.local.SecurityCryptoManager
import com.example.testbankapp.data.local.TokenManager
import com.example.testbankapp.data.repository.AccountRepository
import com.example.testbankapp.data.repository.AccountRepositoryImpl
import com.example.testbankapp.data.repository.AuthRepository
import com.example.testbankapp.data.repository.AuthRepositoryImpl
import com.example.testbankapp.domain.usecase.CreateAccountUseCase
import com.example.testbankapp.domain.usecase.DepositFundsUseCase
import com.example.testbankapp.domain.usecase.GetAccountsUseCase
import com.example.testbankapp.domain.usecase.InitSessionUseCase
import com.example.testbankapp.domain.usecase.IsUserLoggedInUseCase
import com.example.testbankapp.domain.usecase.LoginUseCase
import com.example.testbankapp.domain.usecase.LogoutUseCase
import com.example.testbankapp.domain.usecase.RegisterUseCase
import com.example.testbankapp.presentation.MainViewModel
import com.example.testbankapp.presentation.accounts.AccountsViewModel
//import com.example.testbankapp.presentation.accounts.AccountsViewModel
import com.example.testbankapp.presentation.auth.AuthViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json

val appModule = module {

    single { SecurityCryptoManager() }
    single { TokenManager(androidContext(), get()) }
    single { AuthInterceptor(get()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .build()
    }

    single {
        val networkJson = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/v1/")
            .client(get())
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(AccountApi::class.java) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<AccountRepository> { AccountRepositoryImpl(get()) }

    factory { InitSessionUseCase(get()) }
    factory { IsUserLoggedInUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { GetAccountsUseCase(get()) }
    factory { CreateAccountUseCase(get()) }
    factory { DepositFundsUseCase(get()) }
    factory { LogoutUseCase(get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { AccountsViewModel(get(), get(), get(), get()) }
}
