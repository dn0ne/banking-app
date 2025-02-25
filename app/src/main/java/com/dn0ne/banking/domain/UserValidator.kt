package com.dn0ne.banking.domain

import androidx.annotation.StringRes
import com.dn0ne.banking.R

object UserValidator {
    private val emailRegex = Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+""")

    fun validate(user: User): ValidationResult {
        val emailError = when {
            !user.username.matches(emailRegex) -> R.string.email_is_not_valid
            else -> null
        }

        val passwordError = when {
            user.password.length < 12 -> R.string.password_is_too_short
            else -> null
        }

        return ValidationResult(emailError, passwordError)
    }

    data class ValidationResult(
        @StringRes val emailError: Int?,
        @StringRes val passwordError: Int?
    )
}