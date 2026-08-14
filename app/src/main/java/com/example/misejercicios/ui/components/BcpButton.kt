package com.example.misejercicios.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.ui.theme.BcpBlue
enum class BcpButtonVariant { PRIMARY, SECONDARY, TERTIARY }

@Composable
fun BcpButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: BcpButtonVariant = BcpButtonVariant.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null
) {
    val buttonModifier = modifier
        .fillMaxWidth()
        .height(dimensionResource(R.dimen.button_height))
    val isEnabled = enabled && !isLoading

    val content: @Composable () -> Unit = {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.height(24.dp),
                color = if (variant == BcpButtonVariant.PRIMARY) Color.White else BcpBlue,
                strokeWidth = 2.dp
            )
        } else {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = null, modifier = Modifier.height(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }

    when (variant) {
        BcpButtonVariant.PRIMARY -> Button(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = BcpBlue),
            content = { content() }
        )

        BcpButtonVariant.SECONDARY -> OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = isEnabled,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = BcpBlue),
            border = BorderStroke(1.dp, BcpBlue),
            content = { content() }
        )

        BcpButtonVariant.TERTIARY -> TextButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = isEnabled,
            colors = ButtonDefaults.textButtonColors(contentColor = BcpBlue),
            content = { content() }
        )
    }
}
