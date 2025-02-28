package com.dn0ne.banking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dn0ne.banking.presentation.Routes
import com.dn0ne.banking.presentation.authentication.ApiEvent
import com.dn0ne.banking.presentation.authentication.AuthenticationEvent
import com.dn0ne.banking.presentation.authentication.AuthenticationViewModel
import com.dn0ne.banking.presentation.authentication.LoginScreen
import com.dn0ne.banking.presentation.authentication.RegisteredScreen
import com.dn0ne.banking.presentation.authentication.SignupScreen
import com.dn0ne.banking.presentation.authentication.VerificationScreen
import com.dn0ne.banking.presentation.authentication.WelcomeScreen
import com.dn0ne.banking.presentation.message.ObserveAsEvents
import com.dn0ne.banking.presentation.message.ScaffoldWithMessageEvents
import com.dn0ne.banking.ui.theme.BankingTheme
import com.dn0ne.banking.ui.theme.DarkBlue
import com.dn0ne.banking.ui.theme.DarkPurple
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BankingTheme {
                ScaffoldWithMessageEvents {
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
                                            // TODO: navigate to main route
                                            TODO()
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
                                composable<Routes.Main.Home> {
                                    TODO()
                                }

                                composable<Routes.Main.Transfers> {
                                    TODO()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {
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
            ),
        contentAlignment = Alignment.Center
    ) {
        RegisteredScreen(
            onLoginClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}