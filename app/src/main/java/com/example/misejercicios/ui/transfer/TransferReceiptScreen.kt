package com.example.misejercicios.ui.transfer

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.ui.components.BcpButton
import com.example.misejercicios.ui.components.BcpButtonVariant

@Composable
fun TransferReceiptScreen(
    viewModel: TransferViewModel,
    onBackToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val finishAndGoHome = {
        viewModel.reset()
        onBackToHome()
    }

    BackHandler(onBack = finishAndGoHome)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.spacing_xl)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF1B7A3E),
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_xxl))
            )
            Text(
                text = stringResource(R.string.transfer_success_message),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
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
                    ReceiptRow(stringResource(R.string.transfer_operation_number), uiState.operationNumber ?: "-")
                    ReceiptRow(stringResource(R.string.transfer_date), uiState.operationDateTime ?: "-")
                    HorizontalDivider()
                    ReceiptRow(stringResource(R.string.transfer_amount_label), "${formatMoney(uiState.amount)} ${uiState.currency}")
                    ReceiptRow(stringResource(R.string.transfer_itf_label), "${formatMoney(uiState.itf)} ${uiState.currency}")
                    HorizontalDivider()
                    ReceiptRow(
                        label = stringResource(R.string.transfer_total_label),
                        value = "${formatMoney(uiState.total)} ${uiState.currency}",
                        emphasized = true
                    )
                }
            }

            val shareLabel = stringResource(R.string.transfer_share)
            BcpButton(
                text = shareLabel,
                variant = BcpButtonVariant.SECONDARY,
                leadingIcon = Icons.Filled.Share,
                onClick = {
                    val receiptText = buildString {
                        appendLine(shareLabel)
                        appendLine("N ${uiState.operationNumber}")
                        appendLine(uiState.operationDateTime)
                        appendLine("${formatMoney(uiState.total)} ${uiState.currency}")
                    }
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, receiptText)
                    }
                    context.startActivity(Intent.createChooser(sendIntent, shareLabel))
                }
            )
            BcpButton(
                text = stringResource(R.string.transfer_back_home),
                variant = BcpButtonVariant.PRIMARY,
                onClick = finishAndGoHome
            )
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, emphasized: Boolean = false) {
    Column {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = if (emphasized) 20.sp else 15.sp,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
