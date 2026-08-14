package com.example.misejercicios.ui.operations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.misejercicios.R
import com.example.misejercicios.ui.components.BcpTopAppBar
import com.example.misejercicios.ui.components.QuickAction
import com.example.misejercicios.ui.components.QuickActionItem
import com.example.misejercicios.ui.theme.BcpBlue

@Composable
fun OperationsScreen(onTransferClick: () -> Unit) {
    var showComingSoon by remember { mutableStateOf(false) }
    val items = listOf(
        QuickActionItem(R.string.qa_transfers, Icons.Outlined.SwapHoriz, onTransferClick),
        QuickActionItem(R.string.qa_pay_services, Icons.Outlined.Receipt) { showComingSoon = true },
        QuickActionItem(R.string.qa_qr, Icons.Outlined.QrCode) { showComingSoon = true },
        QuickActionItem(R.string.qa_yape, Icons.Outlined.PhoneAndroid, onTransferClick),
        QuickActionItem(R.string.qa_recharges, Icons.Outlined.PhoneAndroid) { showComingSoon = true },
        QuickActionItem(R.string.qa_token, Icons.Outlined.Security) { showComingSoon = true }
    )

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.operations_title)) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.operations_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            items.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { item -> QuickAction(item = item, modifier = Modifier.weight(1f)) }
                    // Rellena espacio si la ultima fila tiene menos de 3 elementos
                    repeat(3 - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
            }
        }
    }

    if (showComingSoon) {
        AlertDialog(
            onDismissRequest = { showComingSoon = false },
            title = { Text(stringResource(R.string.coming_soon_title)) },
            text = { Text(stringResource(R.string.coming_soon_message)) },
            confirmButton = {
                TextButton(onClick = { showComingSoon = false }) {
                    Text(stringResource(R.string.dialog_confirm), color = BcpBlue)
                }
            }
        )
    }
}
