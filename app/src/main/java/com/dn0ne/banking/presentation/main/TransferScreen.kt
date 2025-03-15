package com.dn0ne.banking.presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.LocalHazeState
import com.dn0ne.banking.LocalLightHazeStyle
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.TransferForm
import com.dn0ne.banking.presentation.components.TransferResult
import dev.chrisbanes.haze.hazeEffect

@Composable
fun TransfersScreen(
    state: BankingState,
    onEvent: (BankingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Spacer(Modifier.fillMaxHeight(.1f))

        Text(
            text = stringResource(R.string.transfer),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 28.dp)
        )

        Spacer(Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
                .hazeEffect(LocalHazeState.current, LocalLightHazeStyle.current)
                .padding(bottom = 120.dp)
        ) {
            var isTransferConfirmed by remember {
                mutableStateOf(false)
            }

            val accounts = remember(state.accountsToTransactions) {
                state.accountsToTransactions.keys.toList().filter {
                    it.isActive
                }
            }

            val fromPagerState = rememberPagerState { accounts.size }

            LaunchedEffect(fromPagerState.currentPage) {
                onEvent(BankingEvent.OnFromAccountIdChange(accounts[fromPagerState.currentPage]))
            }

            var toAccountIdValue by remember {
                mutableStateOf("")
            }
            val toPagerState = rememberPagerState { accounts.size + 1 }
            val idRegex = remember {
                Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
            }

            LaunchedEffect(toPagerState.currentPage, toAccountIdValue) {
                val value = if (toPagerState.currentPage == 0) {
                    toAccountIdValue
                } else {
                    accounts[toPagerState.currentPage - 1].id
                }

                onEvent(BankingEvent.OnToAccountIdChange(value))
            }

            AnimatedContent(
                targetState = isTransferConfirmed
            ) { isConfirmed ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (isConfirmed) {
                        false -> {
                            TransferForm(
                                fromPagerState = fromPagerState,
                                toPagerState = toPagerState,
                                accounts = accounts,
                                toAccountIdValue = toAccountIdValue,
                                onToAccountIdValueChange = { value ->
                                    toAccountIdValue = value
                                },
                                amountValue = state.amount,
                                onAmountValueChange = {
                                    onEvent(BankingEvent.OnAmountChange(it))
                                },
                                fromAccountId = state.fromAccountId,
                                toAccountId = state.toAccountId,
                                idRegex = idRegex,
                                onContinueClick = {
                                    isTransferConfirmed = true
                                    onEvent(BankingEvent.OnConfirmTransfer)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        true -> {
                            TransferResult(
                                state = state,
                                onBackClick = {
                                    isTransferConfirmed = false
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

        }
    }
}