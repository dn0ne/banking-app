package com.dn0ne.banking.presentation.main

import com.dn0ne.banking.domain.Account
import com.dn0ne.banking.domain.Transaction

data class BankingState(
    val accountsToTransactions: Map<Account, List<Transaction>> = emptyMap()
)
