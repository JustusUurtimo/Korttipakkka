package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.managers.get

@Composable
fun CardView(entityId: Int = 1, activateCard: () -> Unit, modifier: Modifier) {
    val image = (entityId get ImageComponent::class).cardImage
    val name = (entityId get NameComponent::class).name
    val description = (entityId get DescriptionComponent::class).description.value

    Card(
        modifier = modifier
            .background(color = Color.Green)
            .scale(0.99f),
        onClick = activateCard

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(image), contentScale = ContentScale.FillBounds
                )
        ) {
            Column(
                modifier = Modifier
                    .align(BiasAlignment(0f, 0.7f))
                    .fillMaxWidth()
            ) {
                Text(
                    text = name,
                    modifier = Modifier
                        .background(color = Color.Cyan)
                        .fillMaxWidth()
                )
                Text(
                    text = description,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Yellow)
                        .fillMaxWidth()
                )
            }
        }
    }
}
