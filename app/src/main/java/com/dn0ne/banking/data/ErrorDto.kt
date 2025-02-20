package com.dn0ne.banking.data

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val error: String
)
