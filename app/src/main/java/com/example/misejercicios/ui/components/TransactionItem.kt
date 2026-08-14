package com.example.misejercicios.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.Transaction
import com.example.misejercicios.data.mock.TransactionCategory

@Composable
fun TransactionItem(transaction: Transaction, modifier: Modifier = Modifier) {
    val isPositive = transaction.amount > 0
    val amountColor = if (isPositive) Color(0xFF1B7A3E) else MaterialTheme.colorScheme.error

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_md)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.spacing_lg)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoria
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_lg))
                    .clip(CircleShape)
                    .background(categoryIconBackground(transaction.category)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon(transaction.category),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaction.date,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isPositive) "+" else ""}${formatProductAmount(transaction.amount)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = amountColor
                )
                Text(
                    text = transaction.currency,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun categoryIcon(category: TransactionCategory) = when (category) {
    TransactionCategory.SUPERMARKET -> Icons.Filled.ShoppingCart
    TransactionCategory.SALARY -> Icons.Filled.Work
    TransactionCategory.ENTERTAINMENT -> Icons.Filled.PlayCircle
    TransactionCategory.TRANSFER -> Icons.Filled.SwapHoriz
    TransactionCategory.RESTAURANT -> Icons.Filled.Restaurant
}

private fun categoryIconBackground(category: TransactionCategory) = when (category) {
    TransactionCategory.SUPERMARKET -> Color(0xFF4CAF50)
    TransactionCategory.SALARY -> Color(0xFF2196F3)
    TransactionCategory.ENTERTAINMENT -> Color(0xFFE91E63)
    TransactionCategory.TRANSFER -> Color(0xFFFF9800)
    TransactionCategory.RESTAURANT -> Color(0xFF9C27B0)
}
