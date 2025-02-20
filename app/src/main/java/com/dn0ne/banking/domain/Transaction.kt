package com.dn0ne.banking.domain

import kotlinx.datetime.Instant

data class Transaction(
    val id: String,
    val fromAccountId: String,
    val toAccountId: String,
    val amount: Double,
    val createdAt: Instant
)
