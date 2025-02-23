package com.dn0ne.banking.data

object ApiConfig {
    private const val BASE_URL = "http://10.0.2.2:8080/api"
    const val REGISTER_ENDPOINT = "$BASE_URL/register"
    const val LOGIN_ENDPOINT = "$BASE_URL/login"
    const val ACCOUNT_ENDPOINT = "$BASE_URL/account"
    const val TRANSACTION_ENDPOINT = "$BASE_URL/transaction"
}