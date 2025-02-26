package com.dn0ne.banking.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {
    @Serializable
    data object Authentication : Routes {
        @Serializable
        data object Welcome

        @Serializable
        data object Login

        @Serializable
        data object Signup

        @Serializable
        data object Verification

        @Serializable
        data object Registered
    }

    @Serializable
    data object Main : Routes {
        @Serializable
        data object Home

        @Serializable
        data object Transfers
    }
}