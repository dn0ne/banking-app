package com.dn0ne.banking.presentation.authentication

sealed interface AuthenticationEvent {
    data object OnLoginClick : AuthenticationEvent
    data object OnConfirmLoginClick : AuthenticationEvent
    data object OnSignupClick : AuthenticationEvent
    data object OnConfirmSignupClick : AuthenticationEvent

    data class OnUsernameChanged(val value: String): AuthenticationEvent
    data class OnPasswordChanged(val value: String): AuthenticationEvent
    data class OnVerificationCodeChanged(val value: String): AuthenticationEvent
}