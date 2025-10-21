package edu.miu.afinal.feature.login.ui

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false,
    val loginSuccess: Boolean = false,
    val error: String? = null,
)
