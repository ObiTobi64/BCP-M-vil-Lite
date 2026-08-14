package com.example.misejercicios.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.misejercicios.R
import com.example.misejercicios.ui.components.BcpButton
import com.example.misejercicios.ui.components.BcpButtonVariant
import com.example.misejercicios.ui.components.BcpTopAppBar
import com.example.misejercicios.ui.components.ConfirmDialog
import com.example.misejercicios.ui.theme.BcpBlue
import com.example.misejercicios.ui.theme.BcpRed

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var showChangePinDialog by remember { mutableStateOf(false) }
    val profile = uiState.profile

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.profile_title), onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + dimensionResource(R.dimen.spacing_lg),
                bottom = dimensionResource(R.dimen.spacing_xl),
                start = dimensionResource(R.dimen.spacing_xl),
                end = dimensionResource(R.dimen.spacing_xl)
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(BcpRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.initials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(profile.fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        profile.documentId,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_lg))) {
                        ProfileInfoRow(Icons.Outlined.Badge, stringResource(R.string.profile_document), profile.documentId)
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileInfoRow(Icons.Filled.Email, stringResource(R.string.profile_email), profile.email)
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileInfoRow(Icons.Filled.Phone, stringResource(R.string.profile_phone), profile.phone)
                    }
                }
            }

            item {
                Text(
                    text = stringResource(R.string.profile_security_section),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.spacing_lg)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Password, contentDescription = null, tint = BcpBlue)
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = stringResource(R.string.profile_change_pin),
                                modifier = Modifier.weight(1f),
                                fontSize = 15.sp
                            )
                            TextButton(onClick = { showChangePinDialog = true }) {
                                Text(stringResource(R.string.dialog_confirm), color = BcpBlue, fontSize = 13.sp)
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(R.dimen.spacing_lg), vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Fingerprint, contentDescription = null, tint = BcpBlue)
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = stringResource(R.string.profile_biometric_toggle),
                                modifier = Modifier.weight(1f),
                                fontSize = 15.sp
                            )
                            Switch(
                                checked = uiState.biometricEnabled,
                                onCheckedChange = viewModel::setBiometricEnabled,
                                colors = SwitchDefaults.colors(checkedTrackColor = BcpBlue)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                BcpButton(
                    text = stringResource(R.string.profile_logout),
                    onClick = { showLogoutConfirm = true },
                    variant = BcpButtonVariant.SECONDARY,
                    leadingIcon = Icons.AutoMirrored.Filled.Logout
                )
            }
        }
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

    if (showChangePinDialog) {
        AlertDialog(
            onDismissRequest = { showChangePinDialog = false },
            title = { Text(stringResource(R.string.profile_change_pin_dialog_title)) },
            text = { Text(stringResource(R.string.profile_change_pin_dialog_message)) },
            confirmButton = {
                TextButton(onClick = { showChangePinDialog = false }) {
                    Text(stringResource(R.string.dialog_confirm), color = BcpBlue)
                }
            }
        )
    }
}

@Composable
private fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = BcpBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
