package com.sq.thed_ck_licker.player

import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val hpText = "${currentHealth.value.toInt()}/${maxHealth.value.toInt()}"
    val paint = Paint().asFrameworkPaint().apply {
        color = Color.White.toArgb()
        textSize = with(LocalDensity.current) { 16.sp.toPx() }
    }

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
        drawContext.canvas.nativeCanvas.drawText(
            hpText,
            center.x - paint.measureText(hpText) / 2,
            center.y + paint.textSize / 3,
            paint
        )
    }
}

@Composable
fun ScoreDisplayer(score: Int, modifier: Modifier = Modifier) {
    Text("Score: $score steps taken")
}