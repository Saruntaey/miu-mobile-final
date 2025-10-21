package edu.miu.afinal.feature.login.domain.repository

import edu.miu.afinal.feature.login.domain.model.UserCredentials
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun saveUserCredentials(user: UserCredentials)
    fun getUserCredentials(): Flow<UserCredentials?>
}
