package com.dn0ne.banking.presentation.main

enum class TransferState {
    None,
    Processing,
    BadTransaction,
    Success,
    DeviceOffline,
    ServerOffline,
    InsufficientFunds,
    AccountNotFound,
    AccountIsClosed,
    InternalServerError,
    UnknownError
}
