package com.example.misejercicios.ui.profile

import androidx.lifecycle.ViewModel
import com.example.misejercicios.data.mock.MockData
import com.example.misejercicios.data.mock.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val profile: UserProfile = MockData.userProfile,
    val biometricEnabled: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun setBiometricEnabled(enabled: Boolean) {
        _uiState.update { it.copy(biometricEnabled = enabled) }
    }
}
