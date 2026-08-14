package com.example.misejercicios.ui.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnboardingUiState(
    val currentPage: Int = 0,
    val totalPages: Int = 3
)

class OnboardingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun nextPage() {
        _uiState.update { it.copy(currentPage = it.currentPage + 1) }
    }

    fun goToPage(page: Int) {
        _uiState.update { it.copy(currentPage = page) }
    }
}
