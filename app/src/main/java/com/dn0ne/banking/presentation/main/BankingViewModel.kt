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
            is BankingEvent.OnDepositClick -> deposit(event.account, event.amount)
            is BankingEvent.OnWithdrawClick -> withdraw(event.account, event.amount)

            is BankingEvent.OnFromAccountIdChange -> updateFromAccountId(event.account)
            is BankingEvent.OnToAccountIdChange -> updateToAccountId(event.value)
            is BankingEvent.OnAmountChange -> updateAmount(event.value)
            BankingEvent.OnConfirmTransfer -> processTransaction()

            BankingEvent.OnLogoutClick -> reset()
        }
    }

    private fun deposit(account: Account, amount: Double) {
        if (token == null) return

        viewModelScope.launch {
            val result = transactionService.sendTransaction(
                token = token!!,
                fromAccountId = null,
                toAccountId = account.id,
                amount = amount
            )

            when (result) {
                is Result.Success -> refreshData()
                is Result.Error -> {
                    val message = when (result.error) {
                        DataError.Network.NoInternet -> R.string.device_offline
                        DataError.Network.ServerOffline -> R.string.server_offline
                        DataError.Transaction.BadTransaction -> R.string.bad_transaction
                        DataError.Transaction.InsufficientFunds -> R.string.insufficient_funds
                        DataError.Transaction.AccountNotFound -> R.string.account_not_found
                        DataError.Transaction.AccountIsClosed -> R.string.account_is_closed
                        DataError.Transaction.InternalError -> R.string.server_error
                        else -> R.string.unknown_error_occured
                    }

                    MessageController.sendEvent(
                        MessageEvent(message)
                    )
                }
            }
        }
    }

    private fun withdraw(account: Account, amount: Double) {
        if (token == null) return

        viewModelScope.launch {
            val result = transactionService.sendTransaction(
                token = token!!,
                fromAccountId = account.id,
                toAccountId = null,
                amount = amount
            )

            when (result) {
                is Result.Success -> refreshData()
                is Result.Error -> {
                    val message = when (result.error) {
                        DataError.Network.NoInternet -> R.string.device_offline
                        DataError.Network.ServerOffline -> R.string.server_offline
                        DataError.Transaction.BadTransaction -> R.string.bad_transaction
                        DataError.Transaction.InsufficientFunds -> R.string.insufficient_funds
                        DataError.Transaction.AccountNotFound -> R.string.account_not_found
                        DataError.Transaction.AccountIsClosed -> R.string.account_is_closed
                        DataError.Transaction.InternalError -> R.string.server_error
                        else -> R.string.unknown_error_occured
                    }

                    MessageController.sendEvent(
                        MessageEvent(message)
                    )
                }
            }
        }
    }

    private fun processTransaction() {
        if (token == null) return

        viewModelScope.launch {
            _bankingState.update {
                it.copy(
                    transferState = TransferState.Processing
                )
            }

            val fromAccountId = _bankingState.value.fromAccountId
            val toAccountId = _bankingState.value.toAccountId
            val amount = _bankingState.value.amount.toDoubleOrNull() ?: run {
                _bankingState.update {
                    it.copy(
                        transferState = TransferState.BadTransaction
                    )
                }
                return@launch
            }

            val result = transactionService.sendTransaction(
                token = token!!,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                amount = amount
            )

            refreshData()

            when (result) {
                is Result.Success -> {
                    _bankingState.update {
                        it.copy(
                            transferState = TransferState.Success
                        )
                    }
                }
                is Result.Error -> {
                    val state =
                    when (result.error) {
                        DataError.Network.NoInternet -> TransferState.DeviceOffline
                        DataError.Network.ServerOffline -> TransferState.ServerOffline
                        DataError.Network.InternalServerError -> TransferState.InternalServerError
                        DataError.Transaction.BadTransaction -> TransferState.BadTransaction
                        DataError.Transaction.InsufficientFunds -> TransferState.InsufficientFunds
                        DataError.Transaction.AccountNotFound -> TransferState.AccountNotFound
                        DataError.Transaction.AccountIsClosed -> TransferState.AccountIsClosed
                        DataError.Transaction.InternalError -> TransferState.InternalServerError
                        else -> TransferState.UnknownError
                    }

                    _bankingState.update {
                        it.copy(
                            transferState = state
                        )
                    }
                }
            }
        }
    }

    private fun updateFromAccountId(account: Account) {
        _bankingState.update {
            it.copy(
                fromAccountId = account.id
            )
        }
    }

    private fun updateToAccountId(value: String) {
        _bankingState.update {
            it.copy(
                toAccountId = value
            )
        }
    }

    private fun updateAmount(amount: String) {
        amount.toDoubleOrNull()?.let {
            _bankingState.update {
                it.copy(
                    amount = amount.filterNot { char -> char == '-' }
                )
            }
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

    fun setUsername(username: String) {
        _bankingState.update {
            it.copy(
                username = username
            )
        }
    }

    private fun reset() {
        token = null
        _bankingState.update { BankingState() }
    }
}