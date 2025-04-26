package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.ComponentManager


class DeathViewSystem private constructor(private val componentManager: ComponentManager) {
    companion object {
        val instance: DeathViewSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DeathViewSystem(ComponentManager.componentManager)
        }
    }


    @Composable
    fun DeathScreen(
        onRetry: () -> Unit,
        onQuit: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        // Animated appearance
        var visibility by remember { mutableFloatStateOf(0f) }
        LaunchedEffect(Unit) {
            visibility = 1f // Animate in
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

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f * visibility))
                .graphicsLayer { alpha = visibility }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Death message
                Text(
                    "YOU DIED",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .graphicsLayer { alpha = pulseAlpha }
                )

                // Buttons
                Button(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Retry")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onQuit,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("Quit to Menu")
                }
            }
        }
    }
}