package com.example.testbankapp

import android.app.Application
import com.example.testbankapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BankApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BankApplication)
            modules(appModule)
        }
    }
}
