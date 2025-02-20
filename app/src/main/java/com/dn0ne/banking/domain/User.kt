package com.dn0ne.banking.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String
)
