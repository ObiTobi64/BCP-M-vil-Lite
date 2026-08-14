package com.example.misejercicios.ui.home

import androidx.lifecycle.ViewModel
import com.example.misejercicios.data.mock.MockData
import com.example.misejercicios.data.mock.Product
import com.example.misejercicios.data.mock.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val userName: String = MockData.USER_NAME,
    val userInitials: String = MockData.USER_INITIALS,
    val products: List<Product> = MockData.products,
    val transactions: List<Transaction> = MockData.transactions
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
