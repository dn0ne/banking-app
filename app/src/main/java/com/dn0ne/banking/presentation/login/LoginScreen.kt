package com.dn0ne.banking.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dn0ne.banking.presentation.components.BaseLoginForm

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseLoginForm(
        title = "Welcome back!",
        mainButtonText = "Log in",
        onMainButtonClick = onLoginClick,
        footerText = "Don't have an account?",
        footerButtonText = "Sign up",
        onFooterButtonClick = onSignupClick,
        username = "",
        onUsernameChanged = {},
        password = "",
        onPasswordChanged = {},
        modifier = modifier
    )
}