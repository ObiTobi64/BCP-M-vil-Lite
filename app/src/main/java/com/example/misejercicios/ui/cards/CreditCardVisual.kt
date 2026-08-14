package com.example.misejercicios.ui.cards

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.CreditCardDetail
import com.example.misejercicios.ui.theme.BcpBlue
import com.example.misejercicios.ui.theme.BcpBlueDark
import com.example.misejercicios.ui.theme.BcpRed

@Composable
fun CreditCardVisual(
    card: CreditCardDetail,
    modifier: Modifier = Modifier,
    isBlocked: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.credit_card_height))
            .then(clickableModifier),
        shape = RoundedCornerShape(dimensionResource(R.dimen.credit_card_corner_radius)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(BcpBlueDark, BcpBlue, BcpRed.copy(alpha = 0.55f))))
                .padding(22.dp)
        ) {
            // Circulos decorativos
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.06f))
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-40).dp)
            )

            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "BCP",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    if (isBlocked) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.9f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.card_blocked_badge),
                                color = BcpRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Chip decorativo
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFE0C165).copy(alpha = 0.9f))
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = card.maskedNumber,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 3.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = card.holderName,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = card.expiry,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = card.network,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}
