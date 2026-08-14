package com.example.misejercicios.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.MockData
import com.example.misejercicios.ui.components.BcpTopAppBar
import com.example.misejercicios.ui.components.ConfirmDialog
import com.example.misejercicios.ui.theme.BcpBlue
import com.example.misejercicios.ui.theme.BcpRed

@Composable
fun MoreScreen(onProfileClick: () -> Unit, onLogout: () -> Unit) {
    var showComingSoon by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }
    val profile = MockData.userProfile

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.more_title)) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onProfileClick)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(BcpRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(profile.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(profile.fullName, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(
                        profile.email,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            HorizontalDivider()

            MoreMenuItem(
                icon = Icons.Outlined.Person,
                label = stringResource(R.string.more_profile_item),
                onClick = onProfileClick
            )
            MoreMenuItem(
                icon = Icons.Outlined.Settings,
                label = stringResource(R.string.more_settings_item),
                onClick = { showComingSoon = true }
            )
            MoreMenuItem(
                icon = Icons.Outlined.HelpOutline,
                label = stringResource(R.string.more_help_item),
                onClick = { showComingSoon = true }
            )

            HorizontalDivider()

            MoreMenuItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                label = stringResource(R.string.more_logout_item),
                onClick = { showLogoutConfirm = true },
                isDestructive = true
            )
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

    if (showLogoutConfirm) {
        ConfirmDialog(
            title = stringResource(R.string.profile_logout_confirm_title),
            message = stringResource(R.string.profile_logout_confirm_message),
            icon = Icons.AutoMirrored.Filled.Logout,
            isDestructive = true,
            onConfirm = {
                showLogoutConfirm = false
                onLogout()
            },
            onDismiss = { showLogoutConfirm = false }
        )
    }
}

@Composable
private fun MoreMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) BcpRed else BcpBlue
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDestructive) BcpRed else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        if (!isDestructive) {
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
