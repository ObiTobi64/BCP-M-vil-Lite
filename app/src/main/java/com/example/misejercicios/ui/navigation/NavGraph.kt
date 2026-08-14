package com.example.misejercicios.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.misejercicios.R
import com.example.misejercicios.data.mock.MockData
import com.example.misejercicios.ui.cards.CardDetailScreen
import com.example.misejercicios.ui.cards.CardsListScreen
import com.example.misejercicios.ui.components.BcpBottomBar
import com.example.misejercicios.ui.components.BcpBottomBarTab
import com.example.misejercicios.ui.home.HomeScreen
import com.example.misejercicios.ui.login.LoginScreen
import com.example.misejercicios.ui.login.RecoveryScreen
import com.example.misejercicios.ui.more.MoreScreen
import com.example.misejercicios.ui.onboarding.OnboardingScreen
import com.example.misejercicios.ui.operations.OperationsScreen
import com.example.misejercicios.ui.profile.ProfileScreen
import com.example.misejercicios.ui.splash.SplashScreen
import com.example.misejercicios.ui.transfer.TransferConfirmScreen
import com.example.misejercicios.ui.transfer.TransferFormScreen
import com.example.misejercicios.ui.transfer.TransferReceiptScreen
import com.example.misejercicios.ui.transfer.TransferTypeScreen
import com.example.misejercicios.ui.transfer.TransferViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Recovery : Screen("recovery")

    // Pestanas del bottom bar
    object Home : Screen("home")
    object Operations : Screen("operations")
    object Cards : Screen("cards")
    object More : Screen("more")

    object Profile : Screen("profile")

    object CardDetail : Screen("card_detail/{cardId}") {
        fun createRoute(cardId: String) = "card_detail/$cardId"
    }

    // Flujo de transferencias (grafo anidado con ViewModel compartido entre pasos)
    object TransferGraph : Screen("transfer_graph")
    object TransferType : Screen("transfer_type")
    object TransferForm : Screen("transfer_form")
    object TransferConfirm : Screen("transfer_confirm")
    object TransferReceipt : Screen("transfer_receipt")
}

private val bottomBarTabs = listOf(
    BcpBottomBarTab(Screen.Home.route, R.string.tab_inicio, Icons.Outlined.Home, Icons.Filled.Home),
    BcpBottomBarTab(Screen.Operations.route, R.string.tab_operaciones, Icons.Outlined.SwapHoriz, Icons.Filled.SwapHoriz),
    BcpBottomBarTab(Screen.Cards.route, R.string.tab_tarjetas, Icons.Outlined.CreditCard, Icons.Filled.CreditCard),
    BcpBottomBarTab(Screen.More.route, R.string.tab_mas, Icons.Outlined.MoreHoriz, Icons.Filled.MoreHoriz)
)
private val bottomBarRoutes = bottomBarTabs.map { it.route }.toSet()

// Transiciones horizontales suaves para el flujo de transferencias
private val slideInFromRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(initialOffsetX = { it }) + fadeIn()
}
private val slideOutToLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut()
}
private val slideInFromLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn()
}
private val slideOutToRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
}

@Composable
fun BcpNavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onToggleLanguage: () -> Unit = {}
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BcpBottomBar(
                    tabs = bottomBarTabs,
                    currentRoute = currentRoute,
                    onTabSelected = { tab ->
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = if (showBottomBar) Modifier.padding(padding) else Modifier
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onNavigateNext = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onForgotPassword = {
                        navController.navigate(Screen.Recovery.route)
                    }
                )
            }
            composable(Screen.Recovery.route) {
                RecoveryScreen(onBack = { navController.popBackStack() })
            }

            // ---- Pestanas principales ----
            composable(Screen.Home.route) {
                HomeScreen(
                    onNotificationsClick = {},
                    onTransferClick = { navController.navigate(Screen.TransferGraph.route) },
                    onCreditCardClick = {
                        navController.navigate(Screen.CardDetail.createRoute(MockData.creditCard.id))
                    },
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme,
                    onToggleLanguage = onToggleLanguage
                )
            }
            composable(Screen.Operations.route) {
                OperationsScreen(onTransferClick = { navController.navigate(Screen.TransferGraph.route) })
            }
            composable(Screen.Cards.route) {
                CardsListScreen(onCardClick = { cardId ->
                    navController.navigate(Screen.CardDetail.createRoute(cardId))
                })
            }
            composable(Screen.More.route) {
                MoreScreen(
                    onProfileClick = { navController.navigate(Screen.Profile.route) },
                    onLogout = { navController.logoutToLogin() }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = { navController.logoutToLogin() }
                )
            }

            composable(
                route = Screen.CardDetail.route,
                arguments = listOf(navArgument("cardId") { type = NavType.StringType })
            ) {
                CardDetailScreen(onBack = { navController.popBackStack() })
            }

            // ---- Flujo de transferencias ----
            navigation(startDestination = Screen.TransferType.route, route = Screen.TransferGraph.route) {
                composable(
                    Screen.TransferType.route,
                    enterTransition = slideInFromRight,
                    exitTransition = slideOutToLeft,
                    popEnterTransition = slideInFromLeft,
                    popExitTransition = slideOutToRight
                ) { entry ->
                    val transferViewModel = entry.transferViewModel(navController)
                    TransferTypeScreen(
                        onBack = { navController.popBackStack() },
                        onTypeSelected = { type ->
                            transferViewModel.selectType(type)
                            navController.navigate(Screen.TransferForm.route)
                        }
                    )
                }
                composable(
                    Screen.TransferForm.route,
                    enterTransition = slideInFromRight,
                    exitTransition = slideOutToLeft,
                    popEnterTransition = slideInFromLeft,
                    popExitTransition = slideOutToRight
                ) { entry ->
                    val transferViewModel = entry.transferViewModel(navController)
                    TransferFormScreen(
                        viewModel = transferViewModel,
                        onBack = { navController.popBackStack() },
                        onContinue = { navController.navigate(Screen.TransferConfirm.route) }
                    )
                }
                composable(
                    Screen.TransferConfirm.route,
                    enterTransition = slideInFromRight,
                    exitTransition = slideOutToLeft,
                    popEnterTransition = slideInFromLeft,
                    popExitTransition = slideOutToRight
                ) { entry ->
                    val transferViewModel = entry.transferViewModel(navController)
                    TransferConfirmScreen(
                        viewModel = transferViewModel,
                        onBack = { navController.popBackStack() },
                        onAuthorized = {
                            navController.navigate(Screen.TransferReceipt.route) {
                                // El usuario no debe poder volver a la confirmacion con "atras"
                                popUpTo(Screen.TransferType.route) { inclusive = false }
                            }
                        }
                    )
                }
                composable(
                    Screen.TransferReceipt.route,
                    enterTransition = slideInFromRight,
                    exitTransition = slideOutToLeft,
                    popEnterTransition = slideInFromLeft,
                    popExitTransition = slideOutToRight
                ) { entry ->
                    val transferViewModel = entry.transferViewModel(navController)
                    TransferReceiptScreen(
                        viewModel = transferViewModel,
                        onBackToHome = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavBackStackEntry.transferViewModel(navController: NavHostController): TransferViewModel {
    val parentEntry = remember(this) { navController.getBackStackEntry(Screen.TransferGraph.route) }
    return viewModel(parentEntry)
}

private fun NavHostController.logoutToLogin() {
    navigate(Screen.Login.route) {
        popUpTo(graph.findStartDestination().id) { inclusive = true }
        launchSingleTop = true
    }
}
