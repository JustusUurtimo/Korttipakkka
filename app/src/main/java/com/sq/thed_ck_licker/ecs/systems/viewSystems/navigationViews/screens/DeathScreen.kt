package com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sq.thed_ck_licker.helpers.navigation.GameNavigator
import com.sq.thed_ck_licker.helpers.navigation.Screen
import com.sq.thed_ck_licker.viewModels.PlayerViewModel


@Composable
fun DeathScreen(
    onRetry: () -> Unit,
    onLeaveGame: () -> Unit,
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerState by playerViewModel.playerState.collectAsState()

    var visibility by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        visibility = 1f
    }

    val transition = rememberInfiniteTransition()
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val text = "YOU DIED"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f * visibility))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animate each letter individually with a bouncing effect
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                text.forEachIndexed { index, char ->
                    // Infinite up-and-down bounce animation
                    val animatedOffsetY by transition.animateFloat(
                        initialValue = 0f, // Resting position
                        targetValue = -10f, // Bounce up
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 500,
                                delayMillis = index * 100, // Staggered bounce
                                easing = LinearEasing
                            ),
                            repeatMode = RepeatMode.Reverse // Go back down
                        )
                    )

                    Text(
                        text = char.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Red,
                        modifier = Modifier
                            .graphicsLayer(
                                translationY = animatedOffsetY, // Apply Y bounce animation
                                alpha = pulseAlpha // Add pulsing effect
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "SCORE: ${playerState.score}")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Retry")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onLeaveGame ,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Quit to Menu")
            }
        }
    }
}