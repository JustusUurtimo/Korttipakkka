package com.sq.thed_ck_licker.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HealthBar(
    currentHealth: MutableState<Float>,
    maxHealth: MutableState<Float>,
    modifier: Modifier = Modifier
) {
    val progress = (currentHealth.value / maxHealth.value.coerceAtLeast(1f))
        .coerceIn(0f, 1f)

    // Smooth animation
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500)
    )

    Canvas(
        modifier = modifier
            .height(24.dp)
            .fillMaxWidth()
    ) {

        drawRoundRect(
            color = Color.Gray.copy(alpha = 0.3f),
            cornerRadius = CornerRadius(8f),
            size = size
        )

        if (animatedProgress > 0) {
            drawRoundRect(
                color = Color.Red,
                cornerRadius = CornerRadius(8f),
                topLeft = Offset(
                    x = size.width * (1f - animatedProgress),
                    y = 0f
                ),  // ← Right edge anchor
                size = Size(size.width * animatedProgress, size.height)
            )
        }

    }

    Text("player Health: ${currentHealth.value.toInt()}", modifier)
}

@Composable
fun ScoreDisplayer(score: Int, modifier: Modifier = Modifier) {
    Text("Score: $score steps taken")
}