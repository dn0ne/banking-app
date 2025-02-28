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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.LoginButton
import com.dn0ne.banking.ui.theme.BlueDarker

@Composable
fun RegisteredScreen(
    onLoginClick: () -> Unit,
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
                text = stringResource(R.string.account_verified),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.account_verified_supporting_text)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LoginButton(
                text = stringResource(R.string.log_in),
                onClick = onLoginClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                isLoading = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}