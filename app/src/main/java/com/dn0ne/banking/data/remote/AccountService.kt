package com.dn0ne.banking.data.remote

import android.util.Log
import com.dn0ne.banking.data.ApiConfig
import com.dn0ne.banking.data.ErrorDto
import com.dn0ne.banking.domain.Account
import com.dn0ne.banking.domain.result.DataError
import com.dn0ne.banking.domain.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountService(
    private val client: HttpClient
) {

    suspend fun openAccount(token: String): Result<Unit, DataError.Network> =
        withContext(Dispatchers.IO) {
            val response = client.post("${ApiConfig.ACCOUNT_ENDPOINT}/open") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            when (response.status) {
                HttpStatusCode.Created -> Result.Success(Unit)
                HttpStatusCode.Forbidden -> Result.Error(DataError.Network.Forbidden)
                HttpStatusCode.Conflict -> {
                    val error = response.body<ErrorDto>()

                    Log.d("AccountService", "Failed to open account: ${error.error}")
                    Result.Error(DataError.Network.Unknown)
                }
                else -> Result.Error(DataError.Network.Unknown)
            }
        }

    suspend fun getAccounts(token: String): Result<List<Account>, DataError.Network> =
        withContext(Dispatchers.IO) {
            val response = client.get(ApiConfig.ACCOUNT_ENDPOINT) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val accounts = response.body<List<Account>>()

                    Result.Success(accounts)
                }
                HttpStatusCode.Forbidden -> Result.Error(DataError.Network.Forbidden)
                HttpStatusCode.InternalServerError -> Result.Error(DataError.Network.InternalServerError)
                else -> Result.Error(DataError.Network.Unknown)
            }
        }

    suspend fun closeAccount(token: String, account: Account): Result<Unit, DataError.Network> =
        withContext(Dispatchers.IO) {
            if (!account.isActive) return@withContext Result.Error(DataError.Network.Conflict)

            val response = client.post("${ApiConfig.ACCOUNT_ENDPOINT}/close/${account.id}") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            when (response.status) {
                HttpStatusCode.OK -> Result.Success(Unit)
                HttpStatusCode.Forbidden -> Result.Error(DataError.Network.Forbidden)
                HttpStatusCode.Conflict -> {
                    val error = response.body<ErrorDto>()

                    Log.d("AccountService", "Failed to close account: $error")
                    Result.Error(DataError.Network.Unknown)
                }
                else -> Result.Error(DataError.Network.Unknown)
            }
        }

    suspend fun reopenAccount(token: String, account: Account): Result<Unit, DataError.Network> =
        withContext(Dispatchers.IO) {
            if (account.isActive) return@withContext Result.Error(DataError.Network.Conflict)

            val response = client.post("${ApiConfig.ACCOUNT_ENDPOINT}/reopen/${account.id}") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            when (response.status) {
                HttpStatusCode.OK -> Result.Success(Unit)
                HttpStatusCode.Forbidden -> Result.Error(DataError.Network.Forbidden)
                HttpStatusCode.Conflict -> {
                    val error = response.body<ErrorDto>()

                    Log.d("AccountService", "Failed to reopen account: $error")
                    Result.Error(DataError.Network.Unknown)
                }
                else -> Result.Error(DataError.Network.Unknown)
            }
        }
}