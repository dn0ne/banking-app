package com.dn0ne.banking.di

import com.dn0ne.banking.data.remote.UserService
import com.dn0ne.banking.data.remote.AccountService
import com.dn0ne.banking.data.remote.TransactionService
import com.dn0ne.banking.presentation.authentication.AuthenticationViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    singleOf(::UserService)
    singleOf(::AccountService)
    singleOf(::TransactionService)

    viewModelOf(::AuthenticationViewModel)
}