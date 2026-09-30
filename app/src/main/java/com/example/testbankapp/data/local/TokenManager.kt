package com.example.testbankapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_auth_prefs")

class TokenManager(
    private val context: Context,
    private val cryptoManager: SecurityCryptoManager = SecurityCryptoManager()
) {
    @Volatile
    private var cachedToken: String? = null

    suspend fun initToken() {
        val encryptedToken = context.dataStore.data.first()[ENCRYPTED_JWT_TOKEN_KEY]
        cachedToken = encryptedToken?.let {
            runCatching { cryptoManager.decrypt(it) }.getOrNull()
        }
    }

    fun getToken(): String? = cachedToken

    suspend fun saveToken(token: String) {
        val encryptedToken = cryptoManager.encrypt(token)
        context.dataStore.edit { preferences ->
            preferences[ENCRYPTED_JWT_TOKEN_KEY] = encryptedToken
        }
        cachedToken = token
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(ENCRYPTED_JWT_TOKEN_KEY)
        }
        cachedToken = null
    }

    companion object {
        private val ENCRYPTED_JWT_TOKEN_KEY = stringPreferencesKey("encrypted_jwt_token")
    }
}
