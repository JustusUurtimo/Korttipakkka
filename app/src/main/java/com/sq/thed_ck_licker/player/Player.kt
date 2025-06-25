package com.sq.thed_ck_licker.player

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler

@Composable
fun HealthBar(
    currentHealth: Float,
    maxHealth: Float,
    modifier: Modifier = Modifier
) {
    val progress = (currentHealth/ maxHealth.coerceAtLeast(1f))
        .coerceIn(0f, 1f)

    // Smooth animation
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500)
    )
    val hpText = "${currentHealth.toInt()}/${maxHealth.toInt()}"
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
fun ScoreDisplay(score: Int) {
    Text("Score: $score steps taken")
}

/**
 * Mainly for debugging for now as descriptions are hard to see
 */
@Composable
fun AdditionalInfoDisplay(latestCard: Int) {
    var name = "-"
    var hp = 0f
    var description = "Nan"
    if (latestCard != -1) {

        try {
        description = (latestCard get EffectComponent::class).toString()
        } catch (_: Exception) {
            Log.v("AdditionalInfoDisplay", "No effect component found for $latestCard")
        }
        try {
            description = TriggerEffectHandler.describe(latestCard)
        }catch (_: Exception) {
            Log.v("AdditionalInfoDisplay", "No Trigger Effect component found for $latestCard")
        }
        name = (latestCard get IdentificationComponent::class).getName()

        try {
            hp = (latestCard get HealthComponent::class).getHealth()
        } catch (_: Exception) {
            Log.v("AdditionalInfoDisplay", "No health component found for $latestCard")
        }
    }
    Text("Info")
    Text("Name: $name")
    Text("Health: ${hp.toInt()}")
    Text("Desc: \n$description")
}