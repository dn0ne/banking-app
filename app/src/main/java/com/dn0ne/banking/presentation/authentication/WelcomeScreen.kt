package com.dn0ne.banking.presentation.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.R
import com.dn0ne.banking.presentation.components.Logo
import com.dn0ne.banking.ui.theme.Blue
import com.dn0ne.banking.ui.theme.BlueDark

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Logo(modifier = Modifier.align(Alignment.Center))

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onLoginClick,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue.copy(alpha = .3f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    text = stringResource(R.string.log_in),
                )
            }

            Button(
                onClick = onSignupClick,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueDark.copy(alpha = .3f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                )
            }
        }
    }
}