package com.example.misejercicios.ui.login

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.ui.theme.BcpBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryScreen(onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }
    val emptyEmailError = stringResource(R.string.error_empty_email)
    val invalidEmailError = stringResource(R.string.error_invalid_email)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.recovery_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BcpBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.spacing_xxl))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            if (!showSuccess) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_xxl)),
                    tint = BcpBlue
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.recovery_subtitle),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; emailError = null },
                    label = { Text(stringResource(R.string.recovery_email_label)) },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        when {
                            email.isBlank() -> emailError = emptyEmailError
                            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                                emailError = invalidEmailError
                            else -> showSuccess = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.button_height)),
                    colors = ButtonDefaults.buttonColors(containerColor = BcpBlue)
                ) {
                    Text(
                        stringResource(R.string.recovery_button),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.MarkEmailRead,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_xxl)),
                    tint = BcpBlue
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.recovery_success_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.recovery_success_message, email),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.button_height)),
                    colors = ButtonDefaults.buttonColors(containerColor = BcpBlue)
                ) {
                    Text(stringResource(R.string.recovery_back_to_login), fontSize = 16.sp)
                }
            }
        }
    }
}
