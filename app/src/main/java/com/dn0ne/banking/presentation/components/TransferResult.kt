package com.dn0ne.banking.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.main.BankingState
import com.dn0ne.banking.presentation.main.TransferState
import com.dn0ne.banking.ui.theme.Green
import com.dn0ne.banking.ui.theme.Red

@Composable
fun TransferResult(
    state: BankingState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackClick()
    }

    Box(
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = state.transferState,
            modifier = Modifier.align(Alignment.Center)
        ) { state ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state == TransferState.Processing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Icon(
                        imageVector = when (state) {
                            TransferState.Success -> Icons.Rounded.CheckCircleOutline
                            else -> Icons.Rounded.ErrorOutline
                        },
                        contentDescription = null,
                        tint = when (state) {
                            TransferState.Success -> Green
                            else -> Red
                        },
                        modifier = Modifier.size(48.dp)
                    )
                }

                val message = remember(state) {
                    when (state) {
                        TransferState.Processing -> R.string.processing_transfer
                        TransferState.BadTransaction -> R.string.bad_transaction
                        TransferState.Success -> R.string.transfer_success
                        TransferState.DeviceOffline -> R.string.device_offline
                        TransferState.ServerOffline -> R.string.server_offline
                        TransferState.InsufficientFunds -> R.string.insufficient_funds
                        TransferState.AccountNotFound -> R.string.account_not_found
                        TransferState.AccountIsClosed -> R.string.account_is_closed
                        TransferState.InternalServerError -> R.string.server_error
                        else -> R.string.unknown_error_occured
                    }
                }

                Text(
                    text = stringResource(message),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = when (state) {
                        TransferState.Processing -> MaterialTheme.colorScheme.primary
                        TransferState.Success -> Green
                        else -> Red
                    }
                )
            }
        }

        BankingButton(
            text = stringResource(R.string.back),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            isLoading = false,
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}