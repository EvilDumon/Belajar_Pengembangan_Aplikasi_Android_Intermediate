package com.example.mystoryapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val TOKEN_LOGIN = stringPreferencesKey("token")

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_LOGIN] ?: ""
        }
    }

    suspend fun saveToken(token : String){
        dataStore.edit { preferences ->
            preferences[TOKEN_LOGIN] = token
        }
    }

    suspend fun clearToken(){
        dataStore.edit { preferences ->
            preferences[TOKEN_LOGIN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}