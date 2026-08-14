package com.example.misejercicios.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.Product
import com.example.misejercicios.data.mock.ProductType
import com.example.misejercicios.ui.theme.BcpBlue
import com.example.misejercicios.ui.theme.BcpBlueDark
import com.example.misejercicios.ui.theme.BcpBlueLight
import com.example.misejercicios.ui.theme.BcpRed
import java.util.Locale

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    val gradientColors = when (product.type) {
        ProductType.SAVINGS -> listOf(BcpBlue, BcpBlueLight)
        ProductType.CHECKING -> listOf(Color(0xFF1A3C5E), Color(0xFF2E6DA4))
        ProductType.CREDIT -> listOf(BcpBlueDark, BcpRed.copy(alpha = 0.8f))
    }
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.product_card_height))
            .then(clickableModifier),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_lg)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradientColors))
                .padding(20.dp)
        ) {
            // Circulos decorativos
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.07f))
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20).dp)
            )
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
                    .align(Alignment.BottomStart)
                    .offset(x = (-20).dp, y = 20.dp)
            )

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = product.name,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = when (product.type) {
                            ProductType.SAVINGS -> Icons.Outlined.AccountBalanceWallet
                            ProductType.CHECKING -> Icons.Outlined.AccountBalance
                            ProductType.CREDIT -> Icons.Outlined.CreditCard
                        },
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = product.maskedNumber,
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 13.sp,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = formatProductAmount(product.balance),
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = product.currency,
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
        }
    }
}

fun formatProductAmount(amount: Double): String {
    val absAmount = kotlin.math.abs(amount)
    return String.format(Locale.US, "%,.2f", absAmount)
}
