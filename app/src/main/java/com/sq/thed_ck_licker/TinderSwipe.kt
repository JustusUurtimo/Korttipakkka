package com.sq.thed_ck_licker

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

//This is all deepseek code, so be wary
// There is some nice things to learn but it is quite janky and possibly really inperformant
@Deprecated("This is all deepseek code, so it is only for example purposes")
@Composable
fun TinderSwipeScreen() {
    val profiles = listOf("Alex", "Taylor", "Jordan", "Casey", "Riley")
    var currentIndex by remember { mutableIntStateOf(0) }
    var showCard by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (currentIndex < profiles.size) {
            if (showCard) {
                SwipeableCard(
                    name = profiles[currentIndex],
                    onSwipeLeft = {
                        coroutineScope.launch {
                            showCard = false
                            delay(300) // Animation duration
                            currentIndex++
                            showCard = true
                        }
                    },
                    onSwipeRight = {
                        coroutineScope.launch {
                            showCard = false
                            delay(300)
                            currentIndex++
                            showCard = true
                        }
                    }
                )
            } else {
                // Empty box while waiting for next card
                Box(modifier = Modifier.size(300.dp))
            }
        } else {
            Text("No more profiles")
        }
    }
}
@Composable
fun SwipeableCard(
    name: String,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val threshold = 0.65f
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var isSwiped by remember { mutableStateOf(false) }

    // Calculate background color based on swipe position
    val backgroundColor by  derivedStateOf {
        when {
            offsetX > screenWidth.value * threshold -> Color.Green.copy(alpha = 0.2f)
            offsetX < -screenWidth.value * threshold -> Color.Red.copy(alpha = 0.2f)
            else -> Color.White
        }
    }

    // Calculate zone colors with gradient effect
    val zoneColors by derivedStateOf {
        val progress = (offsetX / (screenWidth.value * threshold)).coerceIn(-1f, 1f)
        when {
            progress > 0 -> {
                // Swiping right - fade to green
                val alpha = progress.coerceIn(0f, 1f)
                listOf(
                    Color.White,
                    Color.Green.copy(alpha = alpha * 0.3f),
                    Color.Green.copy(alpha = alpha * 0.6f)
                )
            }
            progress < 0 -> {
                // Swiping left - fade to red
                val alpha = abs(progress).coerceIn(0f, 1f)
                listOf(
                    Color.Red.copy(alpha = alpha * 0.6f),
                    Color.Red.copy(alpha = alpha * 0.3f),
                    Color.White
                )
            }
            else -> listOf(Color.White, Color.White, Color.White)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background zones (visible during drag)
        if (abs(offsetX) > 10f) { // Only show when actually dragging
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left red zone
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    zoneColors[0],
                                    zoneColors[1]
                                )
                            )
                        )
                )

                // Right green zone
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    zoneColors[1],
                                    zoneColors[2]
                                )
                            )
                        )
                )
            }
        }

        // The actual card
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            if (!isSwiped) {
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y * 0.5f
                            }
                        },
                        onDragEnd = {
                            if (isSwiped) return@detectDragGestures

                            val xRatio = offsetX / screenWidth.value

                            when {
                                xRatio > threshold -> {
                                    isSwiped = true
                                    onSwipeRight()
                                }
                                xRatio < -threshold -> {
                                    isSwiped = true
                                    onSwipeLeft()
                                }
                                else -> {
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                            }
                        }
                    )
                }
                .background(backgroundColor, RoundedCornerShape(16.dp))
        ) {
            Text(
                text = name,
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )

            // Like/Nope text indicators
            if (abs(offsetX) > 0) {
                Box(
                    modifier = Modifier
                        .align(if (offsetX > 0) Alignment.TopStart else Alignment.TopEnd)
                        .offset(if (offsetX > 0) 16.dp else (-16).dp)
                ) {
                    Text(
                        text = if (offsetX > 0) "LIKE" else "NOPE",
                        color = if (offsetX > 0) Color.Green else Color.Red,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TinderSwipeScreenPreview() {
    TinderSwipeScreen()
}