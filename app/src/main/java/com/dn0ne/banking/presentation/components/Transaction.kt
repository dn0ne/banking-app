package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.domain.Account
import com.dn0ne.banking.domain.Transaction
import com.dn0ne.banking.ui.theme.Green
import com.dn0ne.banking.ui.theme.Red
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

@Composable
fun Transaction(
    account: Account,
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isOutgoing = transaction.fromAccountId == account.id
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isOutgoing) {
                    Icons.Rounded.NorthEast
                } else Icons.Rounded.SouthWest,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isOutgoing) Red else Green
            )
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = "${if (transaction.fromAccountId == account.id) "-" else "+"}$${transaction.amount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            val format = DateTimeComponents.Format {
                dayOfMonth()
                char('.')
                monthNumber()
                char('.')
                year()
                char(' ')
                hour()
                char(':')
                minute()
            }
            val date = transaction.createdAt.format(format)
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.widthIn(min = 24.dp))

        Column(
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text(
                text = if (isOutgoing) "To ${transaction.toAccountId}" else "From ${transaction.fromAccountId}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}