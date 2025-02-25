package com.dn0ne.banking.presentation.authentication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.BaseLoginForm

@Composable
fun SignupScreen(
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseLoginForm(
        title = stringResource(R.string.sign_up),
        mainButtonText = stringResource(R.string.sign_up),
        footerText = stringResource(R.string.already_have_account),
        footerButtonText = stringResource(R.string.log_in),
        onFooterButtonClick = onLoginClick,
        username = "",
        onUsernameChanged = {},
        password = "",
        onPasswordChanged = {},
        modifier = modifier
    )
}