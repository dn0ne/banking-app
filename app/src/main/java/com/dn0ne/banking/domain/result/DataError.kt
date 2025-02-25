package com.dn0ne.banking.domain.result

sealed interface DataError : Error {
    enum class Network : DataError {
        Conflict,
        VerificationRequired,
        LoginFailed,
        Forbidden,
        WrongVerificationCode,
        Unknown,

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