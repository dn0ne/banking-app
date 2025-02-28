package com.dn0ne.banking.domain.result

sealed interface DataError : Error {
    enum class Network : DataError {
        NoInternet,
        Conflict,
        VerificationRequired,
        LoginFailed,
        Forbidden,
        WrongVerificationCode,
        Unknown,

        ServerOffline,
        InternalServerError
    }

    enum class Transaction : DataError {
        BadTransaction,
        InsufficientFunds,
        AccountNotFound,
        AccountIsClosed,
        InternalError
    }
}