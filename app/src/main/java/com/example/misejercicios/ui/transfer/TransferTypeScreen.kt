package com.example.misejercicios.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.TransferType
import com.example.misejercicios.ui.components.BcpTopAppBar
import com.example.misejercicios.ui.theme.BcpBlue

private data class TransferTypeOption(
    val type: TransferType,
    val titleRes: Int,
    val descRes: Int,
    val icon: ImageVector
)

@Composable
fun TransferTypeScreen(
    onBack: () -> Unit,
    onTypeSelected: (TransferType) -> Unit
) {
    val options = listOf(
        TransferTypeOption(TransferType.BETWEEN_OWN, R.string.transfer_type_between_own, R.string.transfer_type_between_own_desc, Icons.Outlined.CompareArrows),
        TransferTypeOption(TransferType.TO_BCP, R.string.transfer_type_to_bcp, R.string.transfer_type_to_bcp_desc, Icons.Outlined.AccountBalance),
        TransferTypeOption(TransferType.INTERBANK, R.string.transfer_type_interbank, R.string.transfer_type_interbank_desc, Icons.Outlined.SwapHoriz),
        TransferTypeOption(TransferType.YAPE, R.string.transfer_type_yape, R.string.transfer_type_yape_desc, Icons.Outlined.PhoneAndroid)
    )

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.transfer_title), onBack = onBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + dimensionResource(R.dimen.spacing_lg),
                bottom = dimensionResource(R.dimen.spacing_lg),
                start = dimensionResource(R.dimen.spacing_xl),
                end = dimensionResource(R.dimen.spacing_xl)
            )
        ) {
            item {
                Text(
                    text = stringResource(R.string.transfer_type_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(options) { option ->
                TransferTypeCard(option = option, onClick = { onTypeSelected(option.type) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TransferTypeCard(option: TransferTypeOption, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.spacing_lg)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.transfer_type_icon_size))
                    .clip(CircleShape)
                    .background(BcpBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(option.icon, contentDescription = null, tint = BcpBlue)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(option.titleRes),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(option.descRes),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
