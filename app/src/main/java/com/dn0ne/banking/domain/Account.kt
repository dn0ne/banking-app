package com.dn0ne.banking.domain

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val isActive: Boolean,
    val balance: Double
)
