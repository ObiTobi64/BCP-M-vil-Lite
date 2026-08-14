package com.example.misejercicios.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misejercicios.data.mock.MockData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val loginError: String? = null
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, usernameError = null, loginError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, loginError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLogin(
        errorEmptyUsername: String,
        errorEmptyPassword: String,
        errorShortUsername: String,
        errorShortPassword: String,
        errorInvalidCredentials: String
    ) {
        val state = _uiState.value
        var usernameError: String? = null
        var passwordError: String? = null

        when {
            state.username.isBlank() -> usernameError = errorEmptyUsername
            state.username.length < 5 -> usernameError = errorShortUsername
        }
        when {
            state.password.isBlank() -> passwordError = errorEmptyPassword
            state.password.length < 8 -> passwordError = errorShortPassword
        }

        if (usernameError != null || passwordError != null) {
            _uiState.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }
            // Delay simulado 1.5 segundos
            delay(1500)
            if (state.username == MockData.VALID_USERNAME && state.password == MockData.VALID_PASSWORD) {
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, loginError = errorInvalidCredentials) }
            }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(loginError = null) }
    }
}
