package com.example.misejercicios.ui.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.TransferType
import com.example.misejercicios.ui.components.BcpButton
import com.example.misejercicios.ui.components.BcpTopAppBar

@Composable
fun TransferConfirmScreen(
    viewModel: TransferViewModel,
    onBack: () -> Unit,
    onAuthorized: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.transfer_confirm_title), onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.spacing_xl)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
        ) {
            Text(
                text = stringResource(R.string.transfer_confirm_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_lg)),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SummaryRow(stringResource(R.string.transfer_confirm_type), typeLabel(uiState.type))
                    SummaryRow(
                        stringResource(R.string.transfer_confirm_origin),
                        uiState.originAccount?.let { "${it.label} ${it.maskedNumber}" } ?: "-"
                    )
                    SummaryRow(stringResource(R.string.transfer_confirm_beneficiary), uiState.beneficiaryText)
                    SummaryRow(stringResource(R.string.transfer_confirm_concept), uiState.concept)
                    if (uiState.glosa.isNotBlank()) {
                        SummaryRow(stringResource(R.string.transfer_confirm_glosa), uiState.glosa)
                    }
                    HorizontalDivider()
                    SummaryRow(
                        label = stringResource(R.string.transfer_confirm_amount),
                        value = "${formatMoney(uiState.amount)} ${uiState.currency}",
                        emphasized = true
                    )
                }
            }

            BcpButton(
                text = stringResource(R.string.transfer_authorize),
                isLoading = uiState.isLoading,
                onClick = { viewModel.authorize(onAuthorized) },
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, emphasized: Boolean = false) {
    Column {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = if (emphasized) 22.sp else 15.sp,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun typeLabel(type: TransferType?): String = when (type) {
    TransferType.BETWEEN_OWN -> stringResource(R.string.transfer_type_between_own)
    TransferType.TO_BCP -> stringResource(R.string.transfer_type_to_bcp)
    TransferType.INTERBANK -> stringResource(R.string.transfer_type_interbank)
    TransferType.YAPE -> stringResource(R.string.transfer_type_yape)
    null -> "-"
}

fun formatMoney(amount: Double): String {
    val absAmount = kotlin.math.abs(amount)
    return String.format(java.util.Locale.US, "%,.2f", absAmount)
}
