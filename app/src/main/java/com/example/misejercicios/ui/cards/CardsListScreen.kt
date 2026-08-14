package com.example.misejercicios.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.MockData
import com.example.misejercicios.ui.components.BcpTopAppBar

@Composable
fun CardsListScreen(onCardClick: (String) -> Unit) {
    val cards = listOf(MockData.creditCard)

    Scaffold(
        topBar = { BcpTopAppBar(title = stringResource(R.string.cards_title)) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp,
                bottom = 16.dp,
                start = 20.dp,
                end = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cards) { card ->
                CreditCardVisual(card = card, onClick = { onCardClick(card.id) })
            }
        }
    }
}
