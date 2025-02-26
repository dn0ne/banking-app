package com.dn0ne.banking.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.BaseLoginForm

@Composable
fun SignupScreen(
    state: AuthenticationState,
    onEvent: (AuthenticationEvent) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseLoginForm(
        title = stringResource(R.string.sign_up),
        mainButtonText = stringResource(R.string.sign_up),
        onMainButtonClick = {
            onEvent(AuthenticationEvent.OnConfirmSignupClick)
        },
        footerText = stringResource(R.string.already_have_account),
        footerButtonText = stringResource(R.string.log_in),
        onFooterButtonClick = onLoginClick,
        username = state.username,
        onUsernameChanged = {
            onEvent(AuthenticationEvent.OnUsernameChanged(it))
        },
        password = state.password,
        onPasswordChanged = {
            onEvent(AuthenticationEvent.OnPasswordChanged(it))
        },
        isLoading = state.isLoading,
        modifier = modifier
    )
}