package com.dn0ne.banking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dn0ne.banking.R

@Composable
fun BankingAmountDialog(
    title: String,
    onConfirmClick: (Double) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            var amountValue by remember {
                mutableStateOf("")
            }
            OutlinedTextField(
                value = amountValue,
                onValueChange = {
                    it.toDoubleOrNull()?.let { amount ->
                        if (amount > 0) {
                            amountValue = it.filterNot { char -> char == '-' }
                        }
                    }
                },
                label = {
                    Text(text = stringResource(R.string.amount))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.align(Alignment.End)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(text = stringResource(R.string.cancel))
                }

                TextButton(
                    onClick = {
                        amountValue.toDoubleOrNull()?.let(onConfirmClick)
                        onDismissRequest()
                    }
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
            }
        }
    }
}