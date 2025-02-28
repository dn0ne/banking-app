package com.dn0ne.banking.presentation.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.CodeTextField

@Composable
fun VerificationScreen(
    state: AuthenticationState,
    onEvent: (AuthenticationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(28.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surface.copy(alpha = .9f))
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.verify_email),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append(text = stringResource(R.string.weve_sent_email) + "\n")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(state.username)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            CodeTextField(
                value = state.verificationCode,
                onValueChange = {
                    onEvent(AuthenticationEvent.OnVerificationCodeChanged(it))
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}