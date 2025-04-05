package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.TheGameHandler
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent

class CardDisplaySystem(val componentManager: ComponentManager) {

    /* TODO: This is kind a ok for now
     *  But the sizes and that kind a things need to be re worked,
     *  For now it is fine and the base logic works
     */
    @Composable
    fun EntityDisplay(entityId: Int = 1) {
        val image =
            componentManager.getComponent(entityId, ImageComponent::class).cardImage
        val name = componentManager.getComponent(entityId, NameComponent::class).name
        val description =
            componentManager.getComponent(entityId, DescriptionComponent::class).description


        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(image), contentScale = ContentScale.FillBounds
                )
        ) {
            Column(
                modifier = Modifier
                    .align(BiasAlignment(-0.0f, 0.7f))
                    .fillMaxWidth()
            ) {
                Text(
                    text = name,
                    modifier = Modifier
                        .background(color = Color.Cyan)
                        .fillMaxWidth()
//                        .scale(0.7f)
                )
                Text(
                    text = description,
                    softWrap = true,
                    modifier = Modifier
                        .background(color = Color.Yellow)
                        .fillMaxWidth()
//                        .scale(0.7f),
                )
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DisplayACardPreview2() {
    TheGameHandler.initTheGame()
    val displaySystem = CardDisplaySystem(TheGameHandler.getTheComponents())
    Box(
        modifier = Modifier
            .size(500.dp)
            .background(Color.Magenta)
    ) {
        displaySystem.EntityDisplay(TheGameHandler.getRandomCard()!!.keys.first())
    }
}

