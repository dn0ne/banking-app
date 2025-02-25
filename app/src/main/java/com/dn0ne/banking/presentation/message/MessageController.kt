package com.dn0ne.banking.presentation.message

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object MessageController {

    private val _events = Channel<MessageEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: MessageEvent) {
        _events.send(event)
    }
}