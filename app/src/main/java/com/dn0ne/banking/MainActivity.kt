package com.dn0ne.banking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.dn0ne.banking.presentation.Routes
import com.dn0ne.banking.presentation.authentication.ApiEvent
import com.dn0ne.banking.presentation.authentication.AuthenticationEvent
import com.dn0ne.banking.presentation.authentication.AuthenticationViewModel
import com.dn0ne.banking.presentation.authentication.LoginScreen
import com.dn0ne.banking.presentation.authentication.RegisteredScreen
import com.dn0ne.banking.presentation.authentication.SignupScreen
import com.dn0ne.banking.presentation.authentication.VerificationScreen
import com.dn0ne.banking.presentation.authentication.WelcomeScreen
import com.dn0ne.banking.presentation.components.BottomNavbar
import com.dn0ne.banking.presentation.components.NavbarDestination
import com.dn0ne.banking.presentation.main.BankingViewModel
import com.dn0ne.banking.presentation.main.HomeScreen
import com.dn0ne.banking.presentation.main.TransfersScreen
import com.dn0ne.banking.presentation.message.ObserveAsEvents
import com.dn0ne.banking.presentation.message.ScaffoldWithMessageEvents
import com.dn0ne.banking.ui.theme.BankingTheme
import com.dn0ne.banking.ui.theme.DarkBlue
import com.dn0ne.banking.ui.theme.DarkPurple
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeSource
import org.koin.androidx.viewmodel.ext.android.viewModel

val LocalHazeState = compositionLocalOf { HazeState() }
val LocalLightHazeStyle = compositionLocalOf {
    HazeStyle(
        backgroundColor = Color.White,
        tint = HazeTint(color = Color.White.copy(.85f)),
        blurRadius = 20.dp
    )
}
val LocalDarkHazeStyle = compositionLocalOf {
    HazeStyle(
        backgroundColor = Color.Black,
        tint = HazeTint(color = Color.White.copy(.15f)),
        blurRadius = 20.dp
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val view = LocalView.current
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }

            BankingTheme {
                ScaffoldWithMessageEvents {
                    Box(
                        modifier = Modifier
                            .hazeSource(LocalHazeState.current)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            DarkBlue,
                                            DarkPurple
                                        )
                                    )
                                )
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Routes.Authentication,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                    initialOffset = { it / 10 }
                                ) + fadeIn()
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                    targetOffset = { it / 10 }
                                ) + fadeOut()
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    initialOffset = { it / 10 }
                                ) + fadeIn()
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    targetOffset = { it / 10 }
                                ) + fadeOut()
                            }
                        ) {
                            navigation<Routes.Authentication>(
                                startDestination = Routes.Authentication.Welcome
                            ) {
                                val viewModel by viewModel<AuthenticationViewModel>()

                                composable<Routes.Authentication.Welcome> {
                                    WelcomeScreen(
                                        onLoginClick = {
                                            viewModel.onEvent(AuthenticationEvent.OnLoginClick)
                                            navController.navigate(Routes.Authentication.Login)
                                        },
                                        onSignupClick = {
                                            viewModel.onEvent(AuthenticationEvent.OnSignupClick)
                                            navController.navigate(Routes.Authentication.Signup)
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                composable<Routes.Authentication.Signup> {
                                    val state by viewModel.authenticationState.collectAsState()
                                    SignupScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        onLoginClick = {
                                            viewModel.onEvent(AuthenticationEvent.OnLoginClick)
                                            navController.navigate(Routes.Authentication.Login) {
                                                popUpTo(Routes.Authentication.Welcome) {
                                                    inclusive = false
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    ObserveAsEvents(viewModel.apiEvents) { event ->
                                        if (event == ApiEvent.Registered) {
                                            navController.navigate(Routes.Authentication.Verification)
                                        }
                                    }
                                }

                                composable<Routes.Authentication.Login> {
                                    val state by viewModel.authenticationState.collectAsState()
                                    LoginScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        onSignupClick = {
                                            viewModel.onEvent(AuthenticationEvent.OnSignupClick)
                                            navController.navigate(Routes.Authentication.Signup) {
                                                popUpTo(Routes.Authentication.Welcome) {
                                                    inclusive = false
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    ObserveAsEvents(viewModel.apiEvents) { event ->
                                        if (event == ApiEvent.LoggedIn) {
                                            viewModel.receivedToken?.let {
                                                navController.navigate(Routes.Main(it)) {
                                                    popUpTo(Routes.Authentication) {
                                                        inclusive = true
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                composable<Routes.Authentication.Verification> {
                                    val state by viewModel.authenticationState.collectAsState()
                                    VerificationScreen(
                                        state = state,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    ObserveAsEvents(viewModel.apiEvents) { event ->
                                        if (event == ApiEvent.Verified) {
                                            navController.navigate(Routes.Authentication.Registered) {
                                                popUpTo(Routes.Authentication.Signup) {
                                                    inclusive = false
                                                }
                                            }
                                        }
                                    }
                                }

                                composable<Routes.Authentication.Registered> {
                                    RegisteredScreen(
                                        onLoginClick = {
                                            navController.navigate(Routes.Authentication.Login) {
                                                popUpTo(Routes.Authentication.Welcome) {
                                                    inclusive = false
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            navigation<Routes.Main>(
                                startDestination = Routes.Main.Home
                            ) {
                                val viewModel by viewModel<BankingViewModel>()

                                composable<Routes.Main.Home> {
                                    val token = navController
                                        .getBackStackEntry<Routes.Main>()
                                        .toRoute<Routes.Main>()
                                        .token
                                    viewModel.setToken(token)

                                    val bankingState by viewModel.bankingState.collectAsState()
                                    HomeScreen(
                                        state = bankingState,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                composable<Routes.Main.Transfers> {
                                    val bankingState by viewModel.bankingState.collectAsState()
                                    TransfersScreen(
                                        state = bankingState,
                                        onEvent = viewModel::onEvent,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }

                        val currentBackStackEntry by navController.currentBackStackEntryAsState()
                        val isInMain by remember {
                            derivedStateOf {
                                currentBackStackEntry?.destination?.parent?.hasRoute<Routes.Main>() == true
                            }
                        }
                        val selectedDestinationIndex by remember {
                            derivedStateOf {
                                when {
                                    currentBackStackEntry?.destination?.hasRoute(Routes.Main.Transfers::class) == true -> 1
                                    else -> 0
                                }
                            }
                        }
                        AnimatedVisibility(
                            visible = isInMain,
                            enter = slideInVertically { it },
                            exit = slideOutVertically { it },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            val destinations = listOf(
                                NavbarDestination(
                                    label = stringResource(R.string.my_cards),
                                    icon = Icons.Rounded.CreditCard,
                                    onClick = {
                                        navController.navigate(Routes.Main.Home) {
                                            popUpTo(Routes.Main.Home)
                                            launchSingleTop = true
                                        }
                                    }
                                ),
                                NavbarDestination(
                                    label = stringResource(R.string.transfers),
                                    icon = Icons.Outlined.Payments,
                                    onClick = {
                                        navController.navigate(Routes.Main.Transfers) {
                                            popUpTo(Routes.Main.Home)
                                            launchSingleTop = true
                                        }
                                    }
                                ),
                                NavbarDestination(
                                    label = stringResource(R.string.profile),
                                    icon = Icons.Outlined.Person,
                                    onClick = {
                                    }
                                )
                            )

                            BottomNavbar(
                                destinations = destinations,
                                selectedDestination = destinations[selectedDestinationIndex],
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
