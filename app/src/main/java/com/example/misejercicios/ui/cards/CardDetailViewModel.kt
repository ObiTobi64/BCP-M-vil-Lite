package com.example.misejercicios.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misejercicios.data.mock.CardConsumption
import com.example.misejercicios.data.mock.CreditCardDetail
import com.example.misejercicios.data.mock.MockData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CardDetailUiState(
    val card: CreditCardDetail = MockData.creditCard,
    val consumptions: List<CardConsumption> = MockData.cardConsumptions,
    val isBlocked: Boolean = false,
    val isProcessing: Boolean = false
)

class CardDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CardDetailUiState())
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

    fun toggleBlock() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            delay(800)
            _uiState.update { it.copy(isProcessing = false, isBlocked = !it.isBlocked) }
        }
    }

    fun pay(onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            delay(1200)
            _uiState.update { it.copy(isProcessing = false) }
            onComplete()
        }
    }
}
