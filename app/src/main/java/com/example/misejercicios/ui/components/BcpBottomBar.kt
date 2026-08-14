package com.example.misejercicios.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.misejercicios.R
import com.example.misejercicios.ui.theme.BcpBlue

data class BcpBottomBarTab(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun BcpBottomBar(
    tabs: List<BcpBottomBarTab>,
    currentRoute: String?,
    onTabSelected: (BcpBottomBarTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) tab.selectedIcon else tab.icon,
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(R.dimen.bottom_bar_icon_size))
                    )
                },
                label = { Text(stringResource(tab.labelRes), fontSize = 11.sp) },
                selected = selected,
                onClick = { onTabSelected(tab) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BcpBlue,
                    selectedTextColor = BcpBlue,
                    indicatorColor = BcpBlue.copy(alpha = 0.12f)
                )
            )
        }
    }
}
