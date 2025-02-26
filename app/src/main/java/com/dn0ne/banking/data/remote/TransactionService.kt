package com.dn0ne.banking.data.remote

import android.util.Log
import com.dn0ne.banking.data.ApiConfig
import com.dn0ne.banking.data.ErrorDto
import com.dn0ne.banking.domain.Account
import com.dn0ne.banking.domain.Transaction
import com.dn0ne.banking.domain.result.DataError
import com.dn0ne.banking.domain.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.net.ConnectException

class TransactionService(
    private val client: HttpClient
) {

    suspend fun getTransactions(
        token: String,
        account: Account
    ): Result<List<Transaction>, DataError.Network> =
        withContext(Dispatchers.IO) {
            val response = try {
                client.get("${ApiConfig.TRANSACTION_ENDPOINT}/${account.id}") {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            } catch (e: HttpRequestTimeoutException) {
                return@withContext Result.Error(DataError.Network.ServerOffline)
            } catch (e: ConnectException) {
                return@withContext Result.Error(DataError.Network.NoInternet)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val transactions = response.body<List<TransactionDto>>().map {
                        it.toTransaction()
                    }

                    Result.Success(transactions)
                }

                HttpStatusCode.Forbidden -> Result.Error(DataError.Network.Forbidden)
                else -> Result.Error(DataError.Network.Unknown)
            }
        }

    suspend fun sendTransaction(
        token: String,
        fromAccountId: String?,
        toAccountId: String?,
        amount: Double
    ): Result<Unit, DataError> =
        withContext(Dispatchers.IO) {
            if ((fromAccountId == null && toAccountId == null) || amount <= 0)
                return@withContext Result.Error(DataError.Transaction.BadTransaction)

            val response = try {
                client.post(ApiConfig.TRANSACTION_ENDPOINT) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        TransactionRequest(
                            fromAccountId = fromAccountId,
                            toAccountId = toAccountId,
                            amount = amount
                        )
                    )
                }
            } catch (e: HttpRequestTimeoutException) {
                return@withContext Result.Error(DataError.Network.ServerOffline)
            } catch (e: ConnectException) {
                return@withContext Result.Error(DataError.Network.NoInternet)
            }


            when (response.status) {
                HttpStatusCode.OK -> Result.Success(Unit)
                HttpStatusCode.Forbidden -> Result.Error(DataError.Network.Forbidden)
                HttpStatusCode.Conflict -> {
                    val error = response.body<ErrorDto>()

                    Log.d("TransactionService", "Failed to process transaction: ${error.error}")
                    Result.Error(DataError.Transaction.valueOf(error.error))
                }

                else -> Result.Error(DataError.Network.Unknown)
            }
        }
}

@Serializable
private data class TransactionDto(
    val id: String,
    val fromAccountId: String,
    val toAccountId: String,
    val amount: Double,
    val createdAt: String
)

private fun TransactionDto.toTransaction() =
    Transaction(
        id = id,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        amount = amount,
        createdAt = Instant.parse(createdAt)
    )

@Serializable
private data class TransactionRequest(
    val fromAccountId: String?,
    val toAccountId: String?,
    val amount: Double
)