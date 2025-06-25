package com.sq.thed_ck_licker.ecs.systems.viewSystems

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.IdentificationComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.displayInfo

var lastTime = -1L

@Composable
fun CardView(
    cardSize: DpSize,
    fontSize: Float,
    isZoomed: Boolean,
    entityId: Int = 1,
    activateCard: () -> Unit,
    modifier: Modifier,
    onZoomChange: (Int) -> Unit
) {
    val image = (entityId get ImageComponent::class).getImage()
    val name = (entityId get IdentificationComponent::class).getName()
    val description =
        try {
            (entityId get EffectComponent::class).toString()
        } catch (_: Exception) {
            "aaa"
        }
    var cardHealth: HealthComponent? = null
    try {
        cardHealth = (entityId get HealthComponent::class)
    } catch (_: IllegalStateException) {
        Log.i(
            "CardView",
            "No health component found for card \n" +
                    "Yeah yeah, we get it, you are so cool there was no health component"
        )
    }

    val scale by animateFloatAsState(
        targetValue = if (isZoomed) 1.5f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f)
    )

    if (lastTime + 5000 < System.currentTimeMillis()) {
        displayInfo(description)
        lastTime = System.currentTimeMillis()
    }

    Card(
        modifier = modifier
            .padding(4.dp)
            .size(cardSize)
            .pointerInput(isZoomed) {
                if (isZoomed) {
                    detectTapGestures { onZoomChange(entityId) }
                } else {
                    detectTapGestures(
                        onLongPress = {
                            onZoomChange(entityId)
                        },
                        onTap = { activateCard() }
                    )
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = fontSize.sp * 1.2
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )

                Text(
                    text = buildAnnotatedString {
                        appendInlineContent("health_icon", "[icon]")
                        append(cardHealth?.getHealth()?.toString() ?: "inf") // Your value
                    },
                    inlineContent = mapOf(
                        "health_icon" to InlineTextContent(
                            Placeholder(12.sp, 12.sp, PlaceholderVerticalAlign.TextCenter)
                        ) {
                            Icon(Icons.Default.Favorite, "Health", tint = Color.Red)
                        }
                    ),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = fontSize.sp  // Convert Float to TextUnit
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * 1.2).sp
                ),
                maxLines = if (isZoomed) 999 else 6,
                overflow = if (isZoomed) TextOverflow.Visible else TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(color = Color.Yellow)
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
            )


        }

    }

}
