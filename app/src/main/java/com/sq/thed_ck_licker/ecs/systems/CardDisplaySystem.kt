package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent

class CardDisplaySystem private constructor(private val componentManager: ComponentManager) {

    companion object {
        val instance: CardDisplaySystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardDisplaySystem(ComponentManager.componentManager)
        }
    }

    @Composable
    fun EntityDisplay(entityId: Int = 1) {
        val image =
            componentManager.getComponent(entityId, ImageComponent::class).cardImage
        val name = componentManager.getComponent(entityId, NameComponent::class).name
        val description =
            componentManager.getComponent(entityId, DescriptionComponent::class).description.value


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


    @Composable
    fun CardsOnHandView(
        playerCardCount: MutableIntState,
        modifier: Modifier,
        latestCard: MutableIntState,
        activateCard: () -> Unit,
    ) {
        BadgedBox(
            badge = {
                Badge(
                    modifier = Modifier.offset((-20).dp, (5).dp),
                    containerColor = Color.Red
                ) {
                    Text("${playerCardCount.intValue}")
                }
            },
            modifier = modifier
                .width(120.dp)
                .height(170.dp)
                .background(color = Color.Magenta),


            ) {
            // TODO: There might be some modifier that "just rounds the corners"
            //  And then it could be just passed via modifier passing or something
            Card(
                modifier = modifier
                    .background(color = Color.Green)
                    .scale(0.99f),
                onClick = activateCard

            ) {
                EntityDisplay(latestCard.intValue)
            }
        }
    }


}

//todo is this not used, so I commented it out for now 8.4.2025
/*
@Preview(showBackground = true)
@Composable
fun DisplayRandomCardPreview() {
    TheGameHandler.initTheGame()
    val displaySystem = CardDisplaySystem(ComponentManager.componentManager)
    val latestCard = remember { mutableIntStateOf(-1) }
    Box(
        modifier = Modifier
            .size(500.dp)
            .background(Color.Magenta)
    ) {
        displaySystem.EntityDisplay(
            cardsSystem.pullRandomCardFromEntityDeck(
                playerId(),
                latestCard
            )
        )
    }
}
*/


//todo is this not used, so I commented it out for now 8.4.2025
/*
@Preview(showBackground = true)
@Composable
fun CardsOnHandViewPreview(
    @PreviewParameter(
        CardEntityPreviewParameterProvider::class,
        limit = 1
    ) cardEntity: MutableIntState
) {
    val displaySystem = CardDisplaySystem(ComponentManager.componentManager)

    val cardCounter = remember { mutableIntStateOf(69) }
    displaySystem.CardsOnHandView(
        cardCounter,
        Modifier,
        cardEntity
    )
}
*/
