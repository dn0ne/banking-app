package com.dn0ne.banking.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dn0ne.banking.R
import com.dn0ne.banking.data.remote.AccountService
import com.dn0ne.banking.data.remote.TransactionService
import com.dn0ne.banking.domain.Account
import com.dn0ne.banking.domain.Transaction
import com.dn0ne.banking.domain.result.DataError
import com.dn0ne.banking.domain.result.Result
import com.dn0ne.banking.presentation.message.MessageController
import com.dn0ne.banking.presentation.message.MessageEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BankingViewModel(
    private val accountService: AccountService,
    private val transactionService: TransactionService
) : ViewModel() {
    private var token: String? = null

    private val _bankingState = MutableStateFlow(BankingState())
    val bankingState = _bankingState.asStateFlow()

    fun onEvent(event: BankingEvent) {
        if (token == null) return
        when (event) {
            BankingEvent.OnOpenAccountClick -> openAccount()
            is BankingEvent.OnCloseAccountClick -> closeAccount(event.account)
            is BankingEvent.OnReopenAccountClick -> reopenAccount(event.account)
        }
    }

    private fun openAccount() {
        viewModelScope.launch {
            accountService.openAccount(token!!)
            refreshData()
        }
    }

    private fun closeAccount(account: Account) {
        if (!account.isActive) return

        viewModelScope.launch {
            accountService.closeAccount(token!!, account)
            refreshData()
        }
    }

    private fun reopenAccount(account: Account) {
        if (account.isActive) return

        viewModelScope.launch {
            accountService.reopenAccount(token!!, account)
            refreshData()
        }
    }

    private fun refreshData() {
        if (token == null) return

        viewModelScope.launch {
            val accounts = getAccounts() ?: return@launch

            val accountsToTransactions = accounts.associateWith { account ->
                getTransactions(account) ?: return@launch
            }

            _bankingState.update {
                it.copy(
                    accountsToTransactions = accountsToTransactions
                )
            }
        }
    }

    private suspend fun getAccounts(): List<Account>? =
        when (val result = accountService.getAccounts(token!!)) {
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                val message = when (result.error) {
                    DataError.Network.NoInternet -> R.string.device_offline
                    DataError.Network.ServerOffline -> R.string.server_offline
                    DataError.Network.InternalServerError -> R.string.server_error
                    else -> R.string.unknown_error_occured
                }

                MessageController.sendEvent(MessageEvent(message))
                null
            }
        }

    private suspend fun getTransactions(account: Account): List<Transaction>? =
        when (val result = transactionService.getTransactions(token!!, account)) {
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                val message = when (result.error) {
                    DataError.Network.NoInternet -> R.string.device_offline
                    DataError.Network.ServerOffline -> R.string.server_offline
                    else -> R.string.unknown_error_occured
                }

                MessageController.sendEvent(MessageEvent(message))
                null
            }
        }

    fun setToken(token: String) {
        this.token = token
        refreshData()
    }
}