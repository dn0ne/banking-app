package com.dn0ne.banking.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.LocalHazeState
import com.dn0ne.banking.R
import com.dn0ne.banking.domain.Account
import com.dn0ne.banking.domain.Transaction
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun DetailsSheet(
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    account: Account?,
    transactions: List<Transaction>,
    onCloseAccountClick: () -> Unit,
    onReopenAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hazeStyle = HazeStyle(
        backgroundColor = MaterialTheme.colorScheme.surface,
        tint = HazeTint(color = MaterialTheme.colorScheme.surface.copy(.85f)),
        blurRadius = 20.dp
    )
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
            .hazeEffect(LocalHazeState.current, hazeStyle)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.details),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        if (account != null) {
            Row(
                modifier = Modifier.height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\uD83C\uDDFA\uD83C\uDDF8",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            color = Color.Black.copy(.25f),
                            offset = Offset(x = 0f, y = 2f),
                            blurRadius = 8f
                        )
                    )
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = "USD ${account.id.take(4)}*${account.id.takeLast(4)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.weight(1f))

                AnimatedVisibility(
                    visible = !isExpanded
                ) {
                    IosTextButton(
                        text = stringResource(R.string.see),
                        onClick = onExpandClick
                    )
                }
            }

            HorizontalDivider()

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.transactions_history),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(12.dp))

            if (transactions.isNotEmpty()) {

                transactions.take(5).forEach {
                    Transaction(
                        account = account,
                        transaction = it,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))
                }

                if (transactions.size > 5) {
                    IosTextButton(
                        onClick = { TODO() },
                        text = stringResource(R.string.full_history),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.your_transactions_will_appear_here),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(Modifier.padding(start = 48.dp))

            if (account.isActive) {
                DetailsButton(
                    icon = Icons.Outlined.Lock,
                    text = stringResource(R.string.close_account),
                    onClick = onCloseAccountClick,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                DetailsButton(
                    icon = Icons.Outlined.LockOpen,
                    text = stringResource(R.string.reopen_account),
                    onClick = onReopenAccountClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Text(
                text = stringResource(R.string.your_account_details_will_appear_here),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}
