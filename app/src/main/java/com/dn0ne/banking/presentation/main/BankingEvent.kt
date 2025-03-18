package com.dn0ne.banking.presentation.main

import com.dn0ne.banking.domain.Account

sealed interface BankingEvent {
    data object OnOpenAccountClick : BankingEvent
    data class OnCloseAccountClick(val account: Account) : BankingEvent
    data class OnReopenAccountClick(val account: Account) : BankingEvent
    data class OnDepositClick(val account: Account, val amount: Double) : BankingEvent
    data class OnWithdrawClick(val account: Account, val amount: Double) : BankingEvent

    data object OnConfirmTransfer : BankingEvent
    data class OnFromAccountIdChange(val account: Account) : BankingEvent
    data class OnToAccountIdChange(val value: String) : BankingEvent
    data class OnAmountChange(val value: String) : BankingEvent

    data object OnLogoutClick : BankingEvent
}