package com.dn0ne.banking.presentation.authentication

sealed interface ApiEvent {
    data object LoggedIn: ApiEvent
    data object Registered: ApiEvent
    data object Verified: ApiEvent
}