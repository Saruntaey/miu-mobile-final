package edu.miu.afinal.feature.login.data.repository

import edu.miu.afinal.feature.login.data.local.PreferencesDataSource
import edu.miu.afinal.feature.login.domain.model.UserCredentials
import edu.miu.afinal.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

class LoginRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource
): LoginRepository {
    override suspend fun saveUserCredentials(user: UserCredentials) {
        preferencesDataSource.saveUserCredentials(user)
    }

    override fun getUserCredentials(): Flow<UserCredentials?> {
        return preferencesDataSource.getUserCredentials()
    }
}
