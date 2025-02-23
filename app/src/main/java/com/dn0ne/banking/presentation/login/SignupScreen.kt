package com.dn0ne.banking.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dn0ne.banking.presentation.components.BaseLoginForm

@Composable
fun SignupScreen(
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseLoginForm(
        title = "Sign up",
        mainButtonText = "Sign up",
        onMainButtonClick = onSignupClick,
        footerText = "Already have an account?",
        footerButtonText = "Log in",
        onFooterButtonClick = onLoginClick,
        username = "",
        onUsernameChanged = {},
        password = "",
        onPasswordChanged = {},
        modifier = modifier
    )
}