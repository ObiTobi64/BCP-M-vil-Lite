package com.example.misejercicios.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.misejercicios.R
import com.example.misejercicios.ui.theme.BcpBlue
import com.example.misejercicios.ui.theme.BcpRed

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isDestructive: Boolean = false,
    confirmText: String = stringResource(R.string.dialog_confirm),
    dismissText: String = stringResource(R.string.dialog_cancel)
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = icon?.let {
            {
                Icon(
                    it,
                    contentDescription = null,
                    tint = if (isDestructive) BcpRed else BcpBlue,
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText, color = if (isDestructive) BcpRed else BcpBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}
