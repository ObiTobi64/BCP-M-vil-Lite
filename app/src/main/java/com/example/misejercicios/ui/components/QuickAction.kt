package com.example.misejercicios.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.ui.theme.BcpBlue

//Variables Principales para los botones de acceso rapido
data class QuickActionItem(
    val labelRes: Int,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun QuickAction(item: QuickActionItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable(onClick = item.onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.quick_access_icon_size))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)))
                .background(BcpBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = BcpBlue,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_md))
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(item.labelRes),
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
