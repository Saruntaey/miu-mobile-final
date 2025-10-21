package edu.miu.afinal.feature.login.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.miu.afinal.feature.login.domain.model.UserCredentials
import edu.miu.afinal.feature.login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository
): ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    init {
        viewModelScope.launch {
            var userCredentials = loginRepository.getUserCredentials().firstOrNull()
            if (userCredentials != null && userCredentials.username == "admin" && userCredentials.password == "admin") {
                Log.i("LoginViewModel", "Already logged in")
                _loginUiState.update {
                    it.copy(isLoggedIn = true)
                }
            }
        }
    }

    fun onUsernameChanged(username: String) {
        _loginUiState.update {
            it.copy(username = username)
        }
    }

    fun onPasswordChanged(password: String) {
        _loginUiState.update {
            it.copy(password = password)
        }
    }

    fun login() {
        viewModelScope.launch {
            val username = loginUiState.value.username
            val password = loginUiState.value.password

            if (username == "admin" && password == "admin") {
                loginRepository.saveUserCredentials(UserCredentials(username, password))
                Log.i("LoginViewModel", "Login successful")
                _loginUiState.update {
                    it.copy(isLoggedIn = true, loginSuccess = true)
                }
            } else {
                Log.i("LoginViewModel", "Login failed")
            }
        }
    }
}
