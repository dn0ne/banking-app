package com.dn0ne.banking.presentation.main

import com.dn0ne.banking.domain.Account

sealed interface BankingEvent {
    data object OnOpenAccountClick: BankingEvent
    data class OnCloseAccountClick(val account: Account): BankingEvent
    data class OnReopenAccountClick(val account: Account): BankingEvent
}