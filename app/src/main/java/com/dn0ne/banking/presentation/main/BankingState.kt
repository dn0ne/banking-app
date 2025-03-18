package com.dn0ne.banking.presentation.main

import com.dn0ne.banking.domain.Account
import com.dn0ne.banking.domain.Transaction

data class BankingState(
    val username: String = "",
    val accountsToTransactions: Map<Account, List<Transaction>> = emptyMap(),
    val fromAccountId: String = "",
    val toAccountId: String = "",
    val amount: String = "",
    val transferState: TransferState = TransferState.None
)
