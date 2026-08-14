package com.example.misejercicios.ui.cards

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.CardConsumption
import com.example.misejercicios.ui.components.BcpButton
import com.example.misejercicios.ui.components.BcpButtonVariant
import com.example.misejercicios.ui.components.BcpTopAppBar
import com.example.misejercicios.ui.components.ConfirmDialog
import com.example.misejercicios.ui.theme.BcpBlue
import com.example.misejercicios.ui.theme.BcpRed

@Composable
fun CardDetailScreen(
    onBack: () -> Unit,
    viewModel: CardDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showBlockConfirm by remember { mutableStateOf(false) }
    var showPayConfirm by remember { mutableStateOf(false) }
    var showPaySuccess by remember { mutableStateOf(false) }
    var showCvvDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.card_detail_title), onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = dimensionResource(R.dimen.spacing_xl),
                vertical = dimensionResource(R.dimen.spacing_lg)
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
        ) {
            item {
                CreditCardVisual(card = uiState.card, isBlocked = uiState.isBlocked)
            }
            item {
                CardInfoSection(uiState = uiState)
            }
            item {
                CardActionsSection(
                    isBlocked = uiState.isBlocked,
                    isProcessing = uiState.isProcessing,
                    onBlockClick = { showBlockConfirm = true },
                    onPayClick = { showPayConfirm = true },
                    onViewCvvClick = { showCvvDialog = true }
                )
            }
            item {
                Text(
                    text = stringResource(R.string.card_recent_consumptions),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(uiState.consumptions) { consumption ->
                ConsumptionItem(consumption)
            }
        }
    }

    if (showBlockConfirm) {
        ConfirmDialog(
            title = stringResource(
                if (uiState.isBlocked) R.string.card_unblock_confirm_title else R.string.card_block_confirm_title
            ),
            message = stringResource(
                if (uiState.isBlocked) R.string.card_unblock_confirm_message else R.string.card_block_confirm_message
            ),
            icon = if (uiState.isBlocked) Icons.Filled.LockOpen else Icons.Filled.Lock,
            isDestructive = !uiState.isBlocked,
            onConfirm = {
                showBlockConfirm = false
                viewModel.toggleBlock()
            },
            onDismiss = { showBlockConfirm = false }
        )
    }

    if (showPayConfirm) {
        ConfirmDialog(
            title = stringResource(R.string.card_pay_confirm_title),
            message = stringResource(
                R.string.card_pay_confirm_message,
                formatMoney(uiState.card.minPayment),
                uiState.card.currency
            ),
            icon = Icons.Filled.Payments,
            onConfirm = {
                showPayConfirm = false
                viewModel.pay { showPaySuccess = true }
            },
            onDismiss = { showPayConfirm = false }
        )
    }

    if (showPaySuccess) {
        AlertDialog(
            onDismissRequest = { showPaySuccess = false },
            title = { Text(stringResource(R.string.card_pay_success)) },
            text = {},
            confirmButton = {
                TextButton(onClick = { showPaySuccess = false }) {
                    Text(stringResource(R.string.dialog_confirm), color = BcpBlue)
                }
            }
        )
    }

    if (showCvvDialog) {
        SecureCvvDialog(cvv = uiState.card.cvv, onDismiss = { showCvvDialog = false })
    }
}

// Sección de información de la tarjeta

@Composable
private fun CardInfoSection(uiState: CardDetailUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_lg))) {
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoItem(
                    label = stringResource(R.string.card_credit_line),
                    value = "${formatMoney(uiState.card.creditLine)} ${uiState.card.currency}",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = stringResource(R.string.card_total_debt),
                    value = "${formatMoney(uiState.card.totalDebt)} ${uiState.card.currency}",
                    modifier = Modifier.weight(1f),
                    valueColor = BcpRed
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoItem(
                    label = stringResource(R.string.card_min_payment),
                    value = "${formatMoney(uiState.card.minPayment)} ${uiState.card.currency}",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = stringResource(R.string.card_cutoff_date),
                    value = uiState.card.cutoffDate,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            InfoItem(
                label = stringResource(R.string.card_payment_due_date),
                value = uiState.card.paymentDueDate
            )
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (valueColor == androidx.compose.ui.graphics.Color.Unspecified) MaterialTheme.colorScheme.onSurface else valueColor
        )
    }
}

@Composable
private fun CardActionsSection(
    isBlocked: Boolean,
    isProcessing: Boolean,
    onBlockClick: () -> Unit,
    onPayClick: () -> Unit,
    onViewCvvClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BcpButton(
            text = stringResource(if (isBlocked) R.string.card_action_unblock else R.string.card_action_block),
            onClick = onBlockClick,
            variant = if (isBlocked) BcpButtonVariant.PRIMARY else BcpButtonVariant.SECONDARY,
            isLoading = isProcessing,
            leadingIcon = if (isBlocked) Icons.Filled.LockOpen else Icons.Filled.Lock,
            modifier = Modifier.weight(1f)
        )
        BcpButton(
            text = stringResource(R.string.card_action_pay),
            onClick = onPayClick,
            variant = BcpButtonVariant.PRIMARY,
            leadingIcon = Icons.Filled.Payments,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    BcpButton(
        text = stringResource(R.string.card_action_view_cvv),
        onClick = onViewCvvClick,
        variant = BcpButtonVariant.TERTIARY,
        leadingIcon = Icons.Filled.Visibility
    )
}

@Composable
private fun ConsumptionItem(consumption: CardConsumption) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_md)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_lg)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = consumption.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = consumption.installments?.let { "${consumption.date} - Cuota $it" } ?: consumption.date,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${formatMoney(consumption.amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Dialogo modal para ver el CVV

@Composable
private fun SecureCvvDialog(cvv: String, onDismiss: () -> Unit) {
    val view = LocalView.current

    DisposableEffect(Unit) {
        val window = (view.context as? Activity)?.window
        window?.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.Visibility, contentDescription = null, tint = BcpBlue) },
        title = { Text(stringResource(R.string.card_cvv_dialog_title)) },
        text = {
            Column {
                Text(
                    text = cvv,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.card_cvv_dialog_warning),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.card_cvv_close), color = BcpBlue)
            }
        }
    )
}

private fun formatMoney(amount: Double): String {
    val absAmount = kotlin.math.abs(amount)
    return String.format(java.util.Locale.US, "%,.2f", absAmount)
}
