package edu.miu.afinal.feature.login.data.local

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import edu.miu.afinal.feature.login.domain.model.UserCredentials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferencesDataSource(
    private val context: Context
) {
    suspend fun saveUserCredentials(user: UserCredentials) {
        context.dataStore.edit { preferences: MutablePreferences ->
            preferences[DataStoreKey.USERNAME] = user.username
            preferences[DataStoreKey.PASSWORD] = user.password
        }
    }

    fun getUserCredentials(): Flow<UserCredentials?> {
        return context.dataStore.data.map { preferences ->
            val username = preferences[DataStoreKey.USERNAME]
            val password = preferences[DataStoreKey.PASSWORD]

            if (username.isNullOrEmpty() ||
                password.isNullOrEmpty()
            ) {
                null
            } else {
                UserCredentials(username = username, password = password)
            }
        }
    }
}
