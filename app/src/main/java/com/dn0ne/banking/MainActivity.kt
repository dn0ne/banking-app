package com.dn0ne.banking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dn0ne.banking.presentation.login.WelcomeScreen
import com.dn0ne.banking.ui.theme.BankingTheme
import com.dn0ne.banking.ui.theme.DarkBlue
import com.dn0ne.banking.ui.theme.DarkPurple

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BankingTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    DarkBlue,
                                    DarkPurple
                                )
                            )
                        )
                ) { innerPadding ->

                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        DarkBlue,
                        DarkPurple
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        WelcomeScreen({}, {}, Modifier.fillMaxSize())
    }
}





@Composable
fun Logo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Bubble(
                modifier = Modifier.matchParentSize()
            )

            Text(
                text = "Bank",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }

        Text(
            text = "App",
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
        )
    }
}

@Composable
fun Bubble(
    modifier: Modifier = Modifier,
    imageOffset: Int = 0
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .shadow(
                8.dp,
                shape = CircleShape,
            )
            .clip(CircleShape)
    ) {
        Image(
            painter = painterResource(R.drawable.gradient),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = when (imageOffset % 3) {
                0 -> Alignment.Center
                1 -> Alignment.CenterStart
                else -> Alignment.CenterEnd
            },
            modifier = Modifier
                .matchParentSize()
        )
    }
}