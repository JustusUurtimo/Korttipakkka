package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.displayInfo

var lastTime = -1L

@Composable
fun CardView(
    isZoomed: Boolean,
    entityId: Int = 1,
    activateCard: () -> Unit,
    modifier: Modifier,
    onZoomChange: (Boolean) -> Unit
) {
    val image = (entityId get ImageComponent::class).getImage()
    val name = (entityId get IdentificationComponent::class).getName()
    val description = (entityId get EffectComponent::class).toString()

    val scale by animateFloatAsState(
        targetValue = if (isZoomed) 1.8f else 1f,
        animationSpec = tween(durationMillis = 200)
    )

    val offsetX by animateDpAsState(
        targetValue = if (isZoomed) 0.dp else 0.dp
    )

    val offsetY by animateDpAsState(
        targetValue = if (isZoomed) (-80).dp else 0.dp
    )

    if (lastTime + 5000 < System.currentTimeMillis()) {
        displayInfo(description)
        lastTime = System.currentTimeMillis()
    }

    Card(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onZoomChange(true)
                        tryAwaitRelease()
                        onZoomChange(false)
                    },
                    onTap = { activateCard() })
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX.toPx()
                translationY = offsetY.toPx()
                shadowElevation = if (isZoomed) 24.dp.toPx() else 8.dp.toPx()
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .zIndex(if (isZoomed) 10f else 0f)
            .background(color = Color.Green)
            .scale(0.99f)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .paint(
                    painterResource(image), contentScale = ContentScale.FillBounds
                )
        ) {
            Column(
                modifier = modifier
                    .align(BiasAlignment(0f, 0.7f))
                    .fillMaxWidth()
            ) {
                Text(
                    text = name,
                    modifier = modifier
                        .background(color = Color.Cyan)
                        .fillMaxWidth()
                )
                Text(
                    text = description,
                    softWrap = true,
                    modifier = modifier
                        .background(color = Color.Yellow)
                        .fillMaxWidth()
                )
            }
        }
    }
}
