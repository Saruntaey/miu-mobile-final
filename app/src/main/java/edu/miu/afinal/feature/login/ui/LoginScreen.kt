package edu.miu.afinal.feature.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.miu.afinal.feature.login.data.local.PreferencesDataSource
import edu.miu.afinal.feature.login.data.repository.LoginRepositoryImpl

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel {
        LoginViewModel(LoginRepositoryImpl(PreferencesDataSource(context)))
    }
    val loginUiState by loginViewModel.loginUiState.collectAsState()

    LaunchedEffect(loginUiState.isLoggedIn) {
        if (loginUiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = loginUiState.username,
                onValueChange = { loginViewModel.onUsernameChanged(it) },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            OutlinedTextField(
                value = loginUiState.password,
                onValueChange = { loginViewModel.onPasswordChanged(it) },
                label = { Text("Password") }
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Button(onClick = { loginViewModel.login() }) {
                Text("Login")
            }
        }
    }
}