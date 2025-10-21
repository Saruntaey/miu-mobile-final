package edu.miu.afinal.feature.login.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKey {
    val USERNAME = stringPreferencesKey("username")
    val PASSWORD = stringPreferencesKey("password")
}