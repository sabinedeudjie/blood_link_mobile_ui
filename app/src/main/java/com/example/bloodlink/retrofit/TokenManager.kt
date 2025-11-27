package com.example.bloodlink.retrofit

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore singleton - must be at top level
private val Context.dataStore by preferencesDataStore("auth_prefs")

class TokenManager(private val context: Context) {

        companion object {
            val TOKEN_KEY = stringPreferencesKey("jwt_token")
        }

        suspend fun saveToken(token: String) {
            context.dataStore.edit { prefs ->
                prefs[TOKEN_KEY] = token
            }
        }

        val tokenFlow: Flow<String?> = context.dataStore.data
            .map { prefs -> prefs[TOKEN_KEY] }
    
}