package com.dn0ne.banking.data.remote

import com.dn0ne.banking.data.ApiConfig
import com.dn0ne.banking.domain.User
import com.dn0ne.banking.domain.result.DataError
import com.dn0ne.banking.domain.result.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.net.ConnectException

class UserService(
    private val client: HttpClient
) {

    suspend fun register(user: User): Result<Unit, DataError.Network> =
        withContext(Dispatchers.IO) {
            val response = try {
                client.post(ApiConfig.REGISTER_ENDPOINT) {
                    contentType(ContentType.Application.Json)
                    setBody(user)
                }
            } catch (e: ConnectException) {
                return@withContext Result.Error(DataError.Network.NoInternet)
            }

            when (response.status) {
                HttpStatusCode.Created -> Result.Success(Unit)
                HttpStatusCode.Unauthorized -> Result.Error(DataError.Network.VerificationRequired)
                HttpStatusCode.Conflict -> Result.Error(DataError.Network.Conflict)
                else -> Result.Error(DataError.Network.Unknown)
            }
        }

    suspend fun login(user: User): Result<String, DataError.Network> =
        withContext(Dispatchers.IO) {
            val response = try {
                client.post(ApiConfig.LOGIN_ENDPOINT) {
                    contentType(ContentType.Application.Json)
                    setBody(user)
                }
            } catch (e: ConnectException) {
                return@withContext Result.Error(DataError.Network.NoInternet)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val token = response.body<TokenDto>()
                    Result.Success(token.token)
                }

                HttpStatusCode.Unauthorized -> Result.Error(DataError.Network.LoginFailed)
                else -> Result.Error(DataError.Network.Unknown)
            }
        }

    suspend fun verify(code: String): Result<Unit, DataError.Network> =
        withContext(Dispatchers.IO) {
            val response = try {
                client.post("${ApiConfig.VERIFICATION_ENDPOINT}/$code")
            } catch (e: ConnectException) {
                return@withContext Result.Error(DataError.Network.NoInternet)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.Success(Unit)
                }

                HttpStatusCode.BadRequest -> Result.Error(DataError.Network.WrongVerificationCode)
                else -> Result.Error(DataError.Network.Unknown)
            }
        }
}

@Serializable
private data class TokenDto(
    val token: String
)