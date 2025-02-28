package com.dn0ne.banking.presentation.message

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScaffoldWithMessageEvents(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var showMessage by remember {
        mutableStateOf(false)
    }
    var message by remember {
        mutableStateOf<MessageEvent?>(null)
    }

    ObserveAsEvents(flow = MessageController.events) { event ->
        coroutineScope.launch {
            showMessage = false
            message = event
            showMessage = true
            delay(3000)
            showMessage = false
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content(it)

            AnimatedVisibility(
                visible = showMessage,
                enter = slideInVertically { it * 2 },
                exit = slideOutVertically { it * 2 },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                val translation = remember {
                    Animatable(0f)
                }
                message?.let {
                    MessageBox(
                        message = it,
                        modifier = Modifier
                            .padding(28.dp)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDrag = { _, dragAmount ->
                                        coroutineScope.launch {
                                            translation.snapTo(
                                                translation.value + dragAmount.y
                                            )
                                        }
                                    },
                                    onDragEnd = {
                                        if (translation.value >= 100) {
                                            showMessage = false
                                        } else {
                                            coroutineScope.launch {
                                                translation.animateTo(0f)
                                            }
                                        }
                                    }
                                )
                            }
                            .graphicsLayer {
                                translationY = translation.value
                            }
                    )
                }
            }
        }
    }
}