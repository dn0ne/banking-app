package com.dn0ne.banking.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.BaseLoginForm

@Composable
fun LoginScreen(
    state: AuthenticationState,
    onEvent: (AuthenticationEvent) -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseLoginForm(
        title = stringResource(R.string.welcome_back),
        mainButtonText = stringResource(R.string.log_in),
        onMainButtonClick = {
            onEvent(AuthenticationEvent.OnConfirmLoginClick)
        },
        footerText = stringResource(R.string.dont_have_account),
        footerButtonText = stringResource(R.string.sign_up),
        onFooterButtonClick = onSignupClick,
        username = state.username,
        usernameError = state.usernameError?.let { stringResource(it) },
        onUsernameChanged = {
            onEvent(AuthenticationEvent.OnUsernameChanged(it))
        },
        password = state.password,
        passwordError = state.passwordError?.let { stringResource(it) },
        onPasswordChanged = {
            onEvent(AuthenticationEvent.OnPasswordChanged(it))
        },
        isLoading = state.isLoading,
        modifier = modifier
    )
}