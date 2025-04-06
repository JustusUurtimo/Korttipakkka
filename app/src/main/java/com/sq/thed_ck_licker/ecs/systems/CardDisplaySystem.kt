package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.TheGameHandler
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.helpers.getRandomElement

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
        cardsDrawCount: MutableIntState,
        modifier: Modifier,
        cardId: Int,
        funn: () -> Unit,
    ) {
//        val thing =
//            SideEffect {
//
//                cardEffectSystem.playerTargetsPlayer(cardId)
//            }
//        val asd = {cardEffectSystem.playerTargetsPlayer(cardId)}

        BadgedBox(
            badge = {
                Badge(
                    modifier = Modifier.offset((-20).dp, (5).dp),
                    containerColor = Color.Red
                ) {
                    Text("${cardsDrawCount.intValue}")
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
                onClick = funn

            ) {
                EntityDisplay(cardId)
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DisplayRandomCardPreview() {
    TheGameHandler.initTheGame()
    val displaySystem = CardDisplaySystem(ComponentManager.componentManager)
    Box(
        modifier = Modifier
            .size(500.dp)
            .background(Color.Magenta)
    ) {
        displaySystem.EntityDisplay(TheGameHandler.getRandomCard()!!.keys.getRandomElement())
    }
}


@Preview(showBackground = true)
@Composable
fun CardsOnHandViewPreview(
    @PreviewParameter(
        CardEntityPreviewParameterProvider::class,
        limit = 1
    ) cardEntity: Int
) {
    val displaySystem = CardDisplaySystem(ComponentManager.componentManager)
    val cardEffectSystem = CardEffectSystem(ComponentManager.componentManager)
    var cardde = TheGameHandler.getRandomCard()!!.keys.getRandomElement()
    val randomCard = {
        cardEffectSystem.playerTargetsPlayer(cardde)
        cardde = TheGameHandler.getRandomCard()!!.keys.getRandomElement()
        println(cardde)
    }

    val cardCounter = remember { mutableIntStateOf(69) }
    displaySystem.CardsOnHandView(
        cardCounter,
        Modifier,
        cardEntity,
        randomCard
    )
}


// TODO: maybe this should be part of test things?
class CardEntityPreviewParameterProvider : PreviewParameterProvider<Int> {
    // TODO here should be some way of actually getting card entities
    init {
        TheGameHandler.initTheGame()
    }

    override val values = TheGameHandler.getTestingCardSequence()
}