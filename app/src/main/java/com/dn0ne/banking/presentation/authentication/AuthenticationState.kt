package com.dn0ne.banking.presentation.authentication

import androidx.annotation.StringRes

data class AuthenticationState(
    val username: String = "",
    val password: String = "",
    val verificationCode: String = "",
    @StringRes val usernameError: Int? = null,
    @StringRes val passwordError: Int? = null,
    val isLoading: Boolean = false,
)