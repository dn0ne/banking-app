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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _authenticationState = MutableStateFlow(AuthenticationState())
    val authenticationState = _authenticationState.asStateFlow()

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            AuthenticationEvent.OnLoginClick -> resetState()
            AuthenticationEvent.OnSignupClick -> resetState()

            AuthenticationEvent.OnConfirmLoginClick -> login()
            AuthenticationEvent.OnConfirmSignupClick -> register()

            is AuthenticationEvent.OnUsernameChanged -> updatePassword(event.value)
            is AuthenticationEvent.OnPasswordChanged -> updateUsername(event.value)
            is AuthenticationEvent.OnVerificationCodeChanged -> updateVerificationCode(event.value)
        }
    }

    private fun resetState() {
        _authenticationState.update { AuthenticationState() }
    }

    private fun updateUsername(value: String) {
        _authenticationState.update {
            it.copy(
                username = value,
                usernameError = null
            )
        }
    }

    private fun updatePassword(value: String) {
        _authenticationState.update {
            it.copy(
                password = value,
                passwordError = null
            )
        }
    }

    private fun updateVerificationCode(value: String) {
        _authenticationState.update {
            it.copy(
                verificationCode = value
            )
        }
    }

    private fun login() {
        _authenticationState.update {
            it.copy(
                isLoading = true
            )
        }

        val user = User(
            username = _authenticationState.value.username,
            password = _authenticationState.value.password
        )

        if (!validateUser(user)) return

        viewModelScope.launch {
            when (val loginResult = userService.login(user)) {
                is Result.Success -> TODO()
                is Result.Error -> {
                    val message = when (loginResult.error) {
                        DataError.Network.LoginFailed -> R.string.login_failed
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
            username = _authenticationState.value.username,
            password = _authenticationState.value.password
        )

        if (!validateUser(user)) return

        viewModelScope.launch {
            when (val loginResult = userService.register(user)) {
                is Result.Success -> {
                    TODO()
                }

                is Result.Error -> {
                    val message = when (loginResult.error) {
                        DataError.Network.VerificationRequired -> R.string.verification_required
                        DataError.Network.Conflict -> R.string.user_already_exists
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