package com.pwhs.quickmem.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pwhs.quickmem.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject


class TokenManager @Inject constructor(private val context: Context) {

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            if (preferences[ACCESS_TOKEN] != null) {
                "Bearer ${preferences[ACCESS_TOKEN]}"
            } else {
                null
            }
        }

    val refreshToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            if (preferences[REFRESH_TOKEN] != null) {
                preferences[REFRESH_TOKEN]
            } else {
                null
            }
        }

    suspend fun saveAccessToken(token: String) {
        Timber.d("Saving access token: $token")
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}