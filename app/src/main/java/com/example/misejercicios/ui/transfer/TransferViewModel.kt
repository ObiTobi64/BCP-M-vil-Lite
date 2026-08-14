package com.example.misejercicios.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misejercicios.data.mock.Account
import com.example.misejercicios.data.mock.Beneficiary
import com.example.misejercicios.data.mock.MockData
import com.example.misejercicios.data.mock.TransferType
import com.example.misejercicios.ui.components.rawDigitsToAmount
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TransferUiState(
    val type: TransferType? = null,
    val accounts: List<Account> = MockData.accounts,
    val originAccountId: String? = null,
    val beneficiaryText: String = "",
    val selectedBeneficiary: Beneficiary? = null,
    val amountRawDigits: String = "",
    val currency: String = "Bs",
    val concept: String = "",
    val glosa: String = "",
    val originError: String? = null,
    val beneficiaryError: String? = null,
    val amountError: String? = null,
    val conceptError: String? = null,
    val glosaError: String? = null,
    val isLoading: Boolean = false,
    val operationNumber: String? = null,
    val operationDateTime: String? = null
) {
    val originAccount: Account? get() = accounts.find { it.id == originAccountId }
    val amount: Double get() = rawDigitsToAmount(amountRawDigits)
    val itf: Double get() = amount * MockData.ITF_RATE
    val total: Double get() = amount + itf
    val isFormValid: Boolean
        get() = originError == null && beneficiaryError == null && amountError == null &&
            conceptError == null && glosaError == null
}

class TransferViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    fun selectType(type: TransferType) {
        _uiState.update { it.copy(type = type) }
    }

    fun selectOrigin(accountId: String) {
        _uiState.update { it.copy(originAccountId = accountId, originError = null, amountError = null) }
    }

    fun onBeneficiaryTextChange(text: String) {
        _uiState.update { it.copy(beneficiaryText = text, selectedBeneficiary = null, beneficiaryError = null) }
    }

    fun selectFrequentBeneficiary(beneficiary: Beneficiary) {
        _uiState.update {
            it.copy(selectedBeneficiary = beneficiary, beneficiaryText = beneficiary.name, beneficiaryError = null)
        }
    }

    fun onAmountRawDigitsChange(raw: String) {
        _uiState.update { it.copy(amountRawDigits = raw, amountError = null) }
    }

    fun onCurrencyChange(currency: String) {
        _uiState.update { it.copy(currency = currency) }
    }

    fun onConceptChange(text: String) {
        _uiState.update { it.copy(concept = text, conceptError = null) }
    }

    fun onGlosaChange(text: String) {
        _uiState.update { it.copy(glosa = text, glosaError = null) }
    }

    fun validateForm(
        errorOrigin: String,
        errorBeneficiary: String,
        errorAmountRequired: String,
        errorAmountZero: String,
        errorInsufficientBalance: String,
        errorConcept: String,
        errorGlosaLength: String
    ): Boolean {
        val state = _uiState.value
        val originError = if (state.originAccountId == null) errorOrigin else null
        val beneficiaryError = if (state.beneficiaryText.isBlank()) errorBeneficiary else null
        val amountError = when {
            state.amountRawDigits.isBlank() || state.amount <= 0.0 -> {
                if (state.amountRawDigits.isBlank()) errorAmountRequired else errorAmountZero
            }
            state.originAccount != null && state.amount > state.originAccount!!.balance -> errorInsufficientBalance
            else -> null
        }
        val conceptError = if (state.concept.isBlank()) errorConcept else null
        val glosaError = if (state.glosa.length > 40) errorGlosaLength else null

        _uiState.update {
            it.copy(
                originError = originError,
                beneficiaryError = beneficiaryError,
                amountError = amountError,
                conceptError = conceptError,
                glosaError = glosaError
            )
        }

        return originError == null && beneficiaryError == null && amountError == null &&
            conceptError == null && glosaError == null
    }

    fun authorize(onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1500)
            val operationNumber = (100_000_000..999_999_999).random().toString()
            val dateTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "BO")).format(Date())
            _uiState.update {
                it.copy(isLoading = false, operationNumber = operationNumber, operationDateTime = dateTime)
            }
            onComplete()
        }
    }

    fun reset() {
        _uiState.value = TransferUiState()
    }
}
