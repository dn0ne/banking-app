package com.dn0ne.banking.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dn0ne.banking.R
import com.dn0ne.banking.data.remote.UserService
import com.dn0ne.banking.domain.User
import com.dn0ne.banking.domain.UserValidator
import com.dn0ne.banking.domain.result.DataError
import com.dn0ne.banking.domain.result.Result
import com.dn0ne.banking.presentation.message.MessageController
import com.dn0ne.banking.presentation.message.MessageEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _authenticationState = MutableStateFlow(AuthenticationState())
    val authenticationState = _authenticationState.asStateFlow()

    private val _apiEvents = Channel<ApiEvent>()
    val apiEvents: Flow<ApiEvent>
        get() = _apiEvents.receiveAsFlow()

    var receivedToken: String? = null
        private set

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            AuthenticationEvent.OnLoginClick -> resetState()
            AuthenticationEvent.OnSignupClick -> resetState()

            AuthenticationEvent.OnConfirmLoginClick -> login()
            AuthenticationEvent.OnConfirmSignupClick -> register()

            is AuthenticationEvent.OnUsernameChanged -> updateUsername(event.value)
            is AuthenticationEvent.OnPasswordChanged -> updatePassword(event.value)
            is AuthenticationEvent.OnVerificationCodeChanged -> updateVerificationCode(event.value)
        }
    }

    private fun resetState() {
        _authenticationState.update { AuthenticationState() }
    }

    private fun updateUsername(value: String) {
        _authenticationState.update {
            it.copy(
                username = value.trimStart()
                    .filter { char -> !char.isWhitespace() && char != '\t' },
                usernameError = null
            )
        }
    }

    private fun updatePassword(value: String) {
        _authenticationState.update {
            it.copy(
                password = value.trimStart()
                    .filter { char -> !char.isWhitespace() && char != '\t' },
                passwordError = null
            )
        }
    }

    private fun updateVerificationCode(value: String) {
        _authenticationState.update {
            it.copy(
                verificationCode = value.filter { char -> char.isDigit() }
            )
        }

        if (_authenticationState.value.verificationCode.length == 6) {
            verify()
        }
    }

    private fun login() {
        _authenticationState.update {
            it.copy(
                isLoading = true
            )
        }

        val user = User(
            username = _authenticationState.value.username.trim(),
            password = _authenticationState.value.password.trim()
        )

        if (!validateUser(user)) {
            _authenticationState.update {
                it.copy(
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            when (val loginResult = userService.login(user)) {
                is Result.Success -> {
                    receivedToken = loginResult.data
                    _apiEvents.send(ApiEvent.LoggedIn)
                }

                is Result.Error -> {
                    val message = when (loginResult.error) {
                        DataError.Network.LoginFailed -> R.string.login_failed
                        DataError.Network.NoInternet -> R.string.device_offline
                        DataError.Network.ServerOffline -> R.string.server_offline
                        else -> R.string.unknown_error_occured
                    }

                    MessageController.sendEvent(MessageEvent(message))
                }
            }

            _authenticationState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun register() {
        _authenticationState.update {
            it.copy(
                isLoading = true
            )
        }

        val user = User(
            username = _authenticationState.value.username.trim(),
            password = _authenticationState.value.password.trim()
        )

        if (!validateUser(user)) {
            _authenticationState.update {
                it.copy(
                    isLoading = false
                )
            }
            return
        }

        viewModelScope.launch {
            when (val registerResult = userService.register(user)) {
                is Result.Success -> _apiEvents.send(ApiEvent.Registered)
                is Result.Error -> {
                    val message = when (registerResult.error) {
                        DataError.Network.VerificationRequired -> {
                            _apiEvents.send(ApiEvent.Registered)
                            R.string.verification_required
                        }

                        DataError.Network.Conflict -> R.string.user_already_exists
                        DataError.Network.NoInternet -> R.string.device_offline
                        DataError.Network.ServerOffline -> R.string.server_offline
                        else -> R.string.unknown_error_occured
                    }

                    MessageController.sendEvent(MessageEvent(message))
                }
            }

            _authenticationState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun verify() {
        viewModelScope.launch {
            _authenticationState.update {
                it.copy(
                    isLoading = true
                )
            }

            val code = _authenticationState.value.verificationCode
            val username = _authenticationState.value.username
            when (val verificationResult = userService.verify(code, username)) {
                is Result.Success -> _apiEvents.send(ApiEvent.Verified)
                is Result.Error -> {
                    _authenticationState.update {
                        it.copy(
                            verificationCode = ""
                        )
                    }

                    val message = when (verificationResult.error) {
                        DataError.Network.WrongVerificationCode -> R.string.verification_code_incorrect
                        DataError.Network.NoInternet -> R.string.device_offline
                        DataError.Network.ServerOffline -> R.string.server_offline
                        else -> R.string.unknown_error_occured
                    }

                    MessageController.sendEvent(MessageEvent(message))
                }
            }

            _authenticationState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun validateUser(user: User): Boolean {
        val validationResult = UserValidator.validate(user)

        if (validationResult.emailError != null || validationResult.passwordError != null) {
            _authenticationState.update {
                it.copy(
                    usernameError = validationResult.emailError,
                    passwordError = validationResult.passwordError
                )
            }
            return false
        }

        return true
    }
}