package com.example.misejercicios.ui.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.Account
import com.example.misejercicios.data.mock.Beneficiary
import com.example.misejercicios.data.mock.MockData
import com.example.misejercicios.ui.components.BcpAmountField
import com.example.misejercicios.ui.components.BcpButton
import com.example.misejercicios.ui.components.BcpTextField
import com.example.misejercicios.ui.components.BcpTopAppBar
import com.example.misejercicios.ui.theme.BcpBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferFormScreen(
    viewModel: TransferViewModel,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFrequentDialog by remember { mutableStateOf(false) }
    var originExpanded by remember { mutableStateOf(false) }

    val errorOrigin = stringResource(R.string.error_origin_required)
    val errorBeneficiary = stringResource(R.string.error_beneficiary_required)
    val errorAmountRequired = stringResource(R.string.error_amount_required)
    val errorAmountZero = stringResource(R.string.error_amount_zero)
    val errorInsufficientBalance = stringResource(R.string.error_insufficient_balance)
    val errorConcept = stringResource(R.string.error_concept_required)
    val errorGlosaLength = stringResource(R.string.error_glosa_too_long)

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.transfer_form_title), onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.spacing_xl)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
        ) {
            // Cuenta origen
            ExposedDropdownMenuBox(expanded = originExpanded, onExpandedChange = { originExpanded = it }) {
                BcpTextField(
                    value = uiState.originAccount?.let { "${it.label} ${it.maskedNumber}" } ?: "",
                    onValueChange = {},
                    label = stringResource(R.string.transfer_origin_account),
                    readOnly = true,
                    isError = uiState.originError != null,
                    errorText = uiState.originError,
                    placeholder = stringResource(R.string.transfer_select_origin),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = originExpanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                )
                ExposedDropdownMenu(expanded = originExpanded, onDismissRequest = { originExpanded = false }) {
                    uiState.accounts.forEach { account ->
                        DropdownMenuItem(
                            text = { Text("${account.label} ${account.maskedNumber}") },
                            onClick = {
                                viewModel.selectOrigin(account.id)
                                originExpanded = false
                            }
                        )
                    }
                }
            }
            uiState.originAccount?.let { account ->
                Text(
                    text = stringResource(R.string.transfer_available_balance, formatAmountText(account.balance), account.currency),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Beneficiario
            BcpTextField(
                value = uiState.beneficiaryText,
                onValueChange = viewModel::onBeneficiaryTextChange,
                label = stringResource(R.string.transfer_beneficiary),
                placeholder = stringResource(R.string.transfer_beneficiary_hint),
                leadingIcon = Icons.Filled.Person,
                isError = uiState.beneficiaryError != null,
                errorText = uiState.beneficiaryError
            )
            TextButton(onClick = { showFrequentDialog = true }) {
                Text(stringResource(R.string.transfer_frequent_beneficiaries), color = BcpBlue, fontSize = 13.sp)
            }

            // Monto y moneda
            BcpAmountField(
                rawDigits = uiState.amountRawDigits,
                onRawDigitsChange = viewModel::onAmountRawDigitsChange,
                label = stringResource(R.string.transfer_amount),
                isError = uiState.amountError != null,
                errorText = uiState.amountError
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CurrencyChip(label = "Bs", selected = uiState.currency == "Bs", onClick = { viewModel.onCurrencyChange("Bs") })
                CurrencyChip(label = "USD", selected = uiState.currency == "USD", onClick = { viewModel.onCurrencyChange("USD") })
            }

            // Concepto
            BcpTextField(
                value = uiState.concept,
                onValueChange = viewModel::onConceptChange,
                label = stringResource(R.string.transfer_concept),
                placeholder = stringResource(R.string.transfer_concept_hint),
                isError = uiState.conceptError != null,
                errorText = uiState.conceptError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Glosa opcional
            BcpTextField(
                value = uiState.glosa,
                onValueChange = { if (it.length <= 40) viewModel.onGlosaChange(it) },
                label = stringResource(R.string.transfer_glosa),
                placeholder = stringResource(R.string.transfer_glosa_hint),
                isError = uiState.glosaError != null,
                errorText = uiState.glosaError ?: "${uiState.glosa.length}/40",
                maxLines = 2,
                singleLine = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(8.dp))

            BcpButton(
                text = stringResource(R.string.transfer_continue),
                onClick = {
                    val isValid = viewModel.validateForm(
                        errorOrigin, errorBeneficiary, errorAmountRequired, errorAmountZero,
                        errorInsufficientBalance, errorConcept, errorGlosaLength
                    )
                    if (isValid) onContinue()
                }
            )
        }
    }

    if (showFrequentDialog) {
        FrequentBeneficiariesDialog(
            beneficiaries = MockData.frequentBeneficiaries,
            onSelect = {
                viewModel.selectFrequentBeneficiary(it)
                showFrequentDialog = false
            },
            onDismiss = { showFrequentDialog = false }
        )
    }
}

@Composable
private fun CurrencyChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BcpBlue,
            selectedLabelColor = androidx.compose.ui.graphics.Color.White
        )
    )
}

@Composable
private fun FrequentBeneficiariesDialog(
    beneficiaries: List<Beneficiary>,
    onSelect: (Beneficiary) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.transfer_frequent_beneficiaries)) },
        text = {
            Column {
                beneficiaries.forEach { beneficiary ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        onClick = { onSelect(beneficiary) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(beneficiary.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(
                                beneficiary.maskedAccount,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_cancel), color = BcpBlue)
            }
        }
    )
}

private fun formatAmountText(amount: Double): String {
    val absAmount = kotlin.math.abs(amount)
    return String.format(java.util.Locale.US, "%,.2f", absAmount)
}
