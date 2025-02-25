package com.dn0ne.banking.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.BaseLoginForm

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseLoginForm(
        title = stringResource(R.string.welcome_back),
        mainButtonText = stringResource(R.string.log_in),
        footerText = stringResource(R.string.dont_have_account),
        footerButtonText = stringResource(R.string.sign_up),
        onFooterButtonClick = onSignupClick,
        username = "",
        onUsernameChanged = {},
        password = "",
        onPasswordChanged = {},
        modifier = modifier
    )
}