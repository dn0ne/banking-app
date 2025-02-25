package com.dn0ne.banking.presentation.message

import androidx.annotation.StringRes

data class MessageEvent(
    @StringRes val message: Int,
    val action: MessageAction? = null
)