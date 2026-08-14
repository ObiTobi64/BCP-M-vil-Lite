package com.example.misejercicios.ui.home

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.Product
import com.example.misejercicios.data.mock.ProductType
import com.example.misejercicios.ui.components.ProductCard
import com.example.misejercicios.ui.components.QuickAction
import com.example.misejercicios.ui.components.QuickActionItem
import com.example.misejercicios.ui.components.TransactionItem
import com.example.misejercicios.ui.theme.BcpBlue
import com.example.misejercicios.ui.theme.BcpRed

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNotificationsClick: () -> Unit = {},
    onTransferClick: () -> Unit = {},
    onCreditCardClick: () -> Unit = {},
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onToggleLanguage: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showComingSoon by remember { mutableStateOf(false) }

    HomeContent(
        uiState = uiState,
        onNotificationsClick = onNotificationsClick,
        onProductClick = { product ->
            if (product.type == ProductType.CREDIT) onCreditCardClick()
        },
        onTransferClick = onTransferClick,
        onOtherQuickAction = { showComingSoon = true },
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onToggleLanguage = onToggleLanguage
    )

    if (showComingSoon) {
        AlertDialog(
            onDismissRequest = { showComingSoon = false },
            title = { Text(stringResource(R.string.coming_soon_title)) },
            text = { Text(stringResource(R.string.coming_soon_message)) },
            confirmButton = {
                TextButton(onClick = { showComingSoon = false }) {
                    Text(stringResource(R.string.dialog_confirm), color = BcpBlue)
                }
            }
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onNotificationsClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    onTransferClick: () -> Unit,
    onOtherQuickAction: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            HomeHeader(
                userName = uiState.userName,
                initials = uiState.userInitials,
                onNotificationsClick = onNotificationsClick,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                onToggleLanguage = onToggleLanguage
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.home_products_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            ProductCarousel(products = uiState.products, onProductClick = onProductClick)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            QuickAccessSection(onTransferClick = onTransferClick, onOtherClick = onOtherQuickAction)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.home_movements_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }
        items(uiState.transactions) { transaction ->
            TransactionItem(
                transaction = transaction,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun HomeHeader(
    userName: String,
    initials: String,
    onNotificationsClick: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(BcpBlue, BcpBlue.copy(alpha = 0.9f)))
            )
            .padding(
                horizontal = dimensionResource(R.dimen.spacing_xl),
                vertical = dimensionResource(R.dimen.spacing_xl)
            )
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.avatar_size))
                        .clip(CircleShape)
                        .background(BcpRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.home_greeting, userName),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.home_subgreeting),
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 13.sp
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Cambiar tema claro/oscuro
                IconButton(onClick = onToggleTheme) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = stringResource(
                            if (isDarkTheme) R.string.action_toggle_theme_to_light else R.string.action_toggle_theme_to_dark
                        ),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                // Cambiar idioma (Espanol/Ingles)
                IconButton(onClick = onToggleLanguage) {
                    Icon(
                        Icons.Filled.Translate,
                        contentDescription = stringResource(R.string.action_toggle_language),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                // Campanita de notificaciones con badge
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = BcpRed,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                ) {
                    IconButton(onClick = onNotificationsClick) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = stringResource(R.string.home_notifications),
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCarousel(products: List<Product>, onProductClick: (Product) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { products.size })
    Column {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.spacing_xl)),
            pageSpacing = dimensionResource(R.dimen.spacing_md),
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val product = products[page]
            ProductCard(product = product, onClick = { onProductClick(product) })
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Indicador de pagina
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(products.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) BcpBlue
                            else BcpBlue.copy(alpha = 0.25f)
                        )
                        .size(
                            if (pagerState.currentPage == index) 20.dp else 7.dp,
                            7.dp
                        )
                )
            }
        }
    }
}

@Composable
private fun QuickAccessSection(onTransferClick: () -> Unit, onOtherClick: () -> Unit) {
    val items = listOf(
        QuickActionItem(R.string.qa_transfers, Icons.Outlined.SwapHoriz, onTransferClick),
        QuickActionItem(R.string.qa_pay_services, Icons.Outlined.Receipt, onOtherClick),
        QuickActionItem(R.string.qa_qr, Icons.Outlined.QrCode, onOtherClick),
        QuickActionItem(R.string.qa_yape, Icons.Outlined.PhoneAndroid, onOtherClick),
        QuickActionItem(R.string.qa_recharges, Icons.Outlined.PhoneAndroid, onOtherClick),
        QuickActionItem(R.string.qa_token, Icons.Outlined.Security, onOtherClick)
    )

    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_xl))) {
        Text(
            text = stringResource(R.string.home_quick_access_title),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            items.take(3).forEach { item -> QuickAction(item = item, modifier = Modifier.weight(1f)) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            items.drop(3).forEach { item -> QuickAction(item = item, modifier = Modifier.weight(1f)) }
        }
    }
}
