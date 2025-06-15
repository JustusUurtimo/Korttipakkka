package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.displayInfo
import kotlin.math.roundToInt

var lastTime = -1L

@Composable
fun CardView(
    cardSize: DpSize,
    isZoomed: Boolean,
    entityId: Int = 1,
    activateCard: () -> Unit,
    modifier: Modifier,
    onZoomChange: (Boolean) -> Unit
) {
    val image = (entityId get ImageComponent::class).getImage()
    val name = (entityId get IdentificationComponent::class).getName()
    val description = (entityId get EffectComponent::class).toString()

    val windowInfo  = LocalWindowInfo.current
    val scale = if (isZoomed) 2f else 1f

    val fontSize by animateFloatAsState(
        targetValue = if (isZoomed) 6f else 10f,
        animationSpec = tween(durationMillis = 200)
    )

    val offset by animateIntOffsetAsState(
        targetValue = if (isZoomed) {
            with(LocalDensity.current) {
                val screenWidthPx = windowInfo.containerSize.width.toFloat()
                val screenHeightPx = windowInfo.containerSize.height.toFloat()
                val scaledWidth = cardSize.width.toPx() * scale
                val scaledHeight = cardSize.height.toPx() * scale

                IntOffset(
                    x = -((screenWidthPx - scaledWidth) / 8).roundToInt(),
                    y = -((screenHeightPx - scaledHeight) / 2).roundToInt()
                )
            }
        } else {
            IntOffset.Zero
        }
    )

    if (lastTime + 5000 < System.currentTimeMillis()) {
        displayInfo(description)
        lastTime = System.currentTimeMillis()
    }

    Card(
        modifier = modifier
            .size(cardSize)
            .pointerInput(isZoomed) {
                if (isZoomed) {
                    detectTapGestures { onZoomChange(false) }
                } else {
                    detectTapGestures(
                        onLongPress = {
                            onZoomChange(true)
                        },
                        onTap = { activateCard() }
                    )
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x.toFloat()
                translationY = offset.y.toFloat()
            }
            .zIndex(if (isZoomed) 10f else 0f)
            .background(color = Color.Green)
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
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = fontSize.sp  // Convert Float to TextUnit
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                        .background(color = Color.Cyan)
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = fontSize.sp  // Same animated size
                    ),
                    maxLines = if (isZoomed) 999 else 2,
                    overflow = if (isZoomed) TextOverflow.Visible else TextOverflow.Ellipsis,
                    softWrap = true,
                    modifier = modifier
                        .background(color = Color.Yellow)
                        .fillMaxWidth()
                )
            }
        }
    }
}
