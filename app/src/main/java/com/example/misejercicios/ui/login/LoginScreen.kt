package com.example.misejercicios.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.misejercicios.R
import com.example.misejercicios.ui.splash.BcpLogoPlaceholder
import com.example.misejercicios.ui.theme.BcpBlue

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    var showBiometricDialog by remember { mutableStateOf(false) }

    val errorEmptyUsername = stringResource(R.string.error_empty_username)
    val errorEmptyPassword = stringResource(R.string.error_empty_password)
    val errorShortUsername = stringResource(R.string.error_short_username)
    val errorShortPassword = stringResource(R.string.error_short_password)
    val errorInvalidCredentials = stringResource(R.string.error_invalid_credentials)

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) onLoginSuccess()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.verticalGradient(listOf(BcpBlue, BcpBlue.copy(alpha = 0.85f)))
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Spacer(modifier = Modifier.height(64.dp))
            BcpLogoPlaceholder()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.login_welcome),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Forma de la card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_xxl)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(dimensionResource(R.dimen.spacing_xxl)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_lg))
                ) {
                    Text(
                        text = stringResource(R.string.login_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Campo del nombre del usuario
                    OutlinedTextField(
                        value = uiState.username,
                        onValueChange = viewModel::onUsernameChange,
                        label = { Text(stringResource(R.string.login_username)) },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        isError = uiState.usernameError != null,
                        supportingText = uiState.usernameError?.let { { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        enabled = !uiState.isLoading
                    )

                    // Campo constraseña
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(stringResource(R.string.login_password)) },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = viewModel::togglePasswordVisibility) {
                                Icon(
                                    imageVector = if (uiState.isPasswordVisible)
                                        Icons.Filled.VisibilityOff
                                    else
                                        Icons.Filled.Visibility,
                                    contentDescription = if (uiState.isPasswordVisible)
                                        stringResource(R.string.login_hide_password)
                                    else
                                        stringResource(R.string.login_show_password)
                                )
                            }
                        },
                        visualTransformation = if (uiState.isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = uiState.passwordError != null,
                        supportingText = uiState.passwordError?.let { { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        enabled = !uiState.isLoading
                    )

                    // Mensaje de error
                    if (uiState.loginError != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_sm))
                        ) {
                            Row(
                                modifier = Modifier.padding(dimensionResource(R.dimen.spacing_md)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.Error,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_sm))
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_sm)))
                                Text(
                                    text = uiState.loginError!!,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // Olvido la constraseña
                    TextButton(
                        onClick = onForgotPassword,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = stringResource(R.string.login_forgot_password),
                            color = BcpBlue,
                            fontSize = 13.sp
                        )
                    }

                    // Boton login
                    Button(
                        onClick = {
                            viewModel.onLogin(
                                errorEmptyUsername,
                                errorEmptyPassword,
                                errorShortUsername,
                                errorShortPassword,
                                errorInvalidCredentials
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.button_height)),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = BcpBlue)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.login_button),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Boton biométrico
                    OutlinedButton(
                        onClick = { showBiometricDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.button_height)),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BcpBlue),
                        border = BorderStroke(1.dp, BcpBlue)
                    ) {
                        Icon(
                            Icons.Outlined.Fingerprint,
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_sm))
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_sm)))
                        Text(
                            text = stringResource(R.string.login_biometric),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Dialogo bimétrico UI
    if (showBiometricDialog) {
        AlertDialog(
            onDismissRequest = { showBiometricDialog = false },
            icon = {
                Icon(
                    Icons.Outlined.Fingerprint,
                    contentDescription = null,
                    tint = BcpBlue,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = { Text(stringResource(R.string.biometric_dialog_title)) },
            text = { Text(stringResource(R.string.biometric_dialog_message)) },
            confirmButton = {
                TextButton(onClick = { showBiometricDialog = false }) {
                    Text(stringResource(R.string.biometric_dialog_ok), color = BcpBlue)
                }
            }
        )
    }
}
