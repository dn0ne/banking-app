package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R
import com.dn0ne.banking.domain.Account

@Composable
fun TransferForm(
    fromPagerState: PagerState,
    toPagerState: PagerState,
    accounts: List<Account>,
    toAccountIdValue: String,
    onToAccountIdValueChange: (String) -> Unit,
    amountValue: String,
    onAmountValueChange: (String) -> Unit,
    fromAccountId: String,
    toAccountId: String,
    idRegex: Regex,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.from_account),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .padding(top = 28.dp, bottom = 16.dp)
        )

        HorizontalPager(
            state = fromPagerState,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { page ->
            AccountSelectorItem(
                account = accounts[page],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            )
        }

        Text(
            text = stringResource(R.string.to_account),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .padding(top = 28.dp, bottom = 16.dp)
        )

        val focusManager = LocalFocusManager.current
        LaunchedEffect(toPagerState.currentPage) {
            focusManager.clearFocus()
        }

        HorizontalPager(
            state = toPagerState,
            verticalAlignment = Alignment.Top,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.height(100.dp)
        ) { page ->
            if (page == 0) {
                TextField(
                    value = toAccountIdValue,
                    onValueChange = onToAccountIdValueChange,
                    placeholder = {
                        Text(text = stringResource(R.string.account_id))
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                        .shadow(
                            elevation = 5.dp,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                )
            } else {
                AccountSelectorItem(
                    account = accounts[page - 1],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        TextField(
            value = amountValue,
            onValueChange = onAmountValueChange,
            label = {
                Text(text = stringResource(R.string.amount))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
        )

        Spacer(Modifier.weight(1f))

        val canContinue = remember(fromAccountId, toAccountId, amountValue) {
            fromAccountId != toAccountId
                    && toAccountId.isNotBlank()
                    && toAccountId.matches(idRegex)
                    && amountValue.isNotBlank()
                    && amountValue.toDoubleOrNull()?.let { it > 0 } == true
        }
        BankingButton(
            text = stringResource(R.string.continue_transfer),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            isLoading = false,
            isEnabled = canContinue,
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}