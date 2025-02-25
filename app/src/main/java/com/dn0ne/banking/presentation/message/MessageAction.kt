package com.dn0ne.banking.presentation.message

import androidx.annotation.StringRes

data class MessageAction(
    @StringRes val name: Int,
    val action: () -> Unit
)