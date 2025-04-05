package com.sq.thed_ck_licker.ui.components.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.TheGameHandler
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardIdentity
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.systems.CardDisplaySystem
import com.sq.thed_ck_licker.helpers.getRandomElement
import kotlinx.coroutines.CancellationException
import kotlin.math.roundToInt

@Composable
fun CardsOnHand(
    cardsOnHand: MutableIntState,
    modifier: Modifier,
    latestCard: Pair<CardIdentity, CardEffect>
) {
    Box {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = Color.Red) {
                            Text("${cardsOnHand.intValue}")
                        }
                    }
                ) {
                    Card {
                        Column {
                            Image(
                                painter = painterResource(id = latestCard.first.cardImage),
                                contentDescription = "Card drawn",
                                modifier = modifier
                                    .width(120.dp)
                                    .wrapContentHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CardsOnHand2(
    cardsOnHand: MutableIntState,
    modifier: Modifier,
    latestCard: Pair<CardIdentity, CardEffect>
) {
    //TODO we should really do "entity carry" it should be just holder class that holds entity id
    // and all its components, it should not be used for data manipulation only to present it in places
    // That way we can carry already fetched entities nicely

    // TODO this really should come here differently..
//    val randomCard = TheGameHandler.getRandomCard()!!.entries.first()
    val randomCard = TheGameHandler.getRandomCard()!!.entries.getRandomElement()
    println("randCard $randomCard")
    val thingsOnIt = TheGameHandler.getComponents(randomCard.key)
    println("thingsOnIt $thingsOnIt") //Maybe we should use logs
    Log.d("CardsOnHand2", "thingsOnIt $thingsOnIt")
    Box {
        Box(
            modifier = modifier
                .align(Alignment.BottomStart)
                .background(color = Color.Blue)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = Color.Red) {
                            Text("${cardsOnHand.intValue}")
                        }
                    }
                ) {
                    Card(
                        modifier = modifier.background(color = Color.Green)
                    ) {
                        Column {
                            Box() {
                                Image(
                                    painter = painterResource(
                                        TheGameHandler.getTheComponents().getComponent(
                                            randomCard.key,
                                            ImageComponent::class
                                        ).cardImage
                                    ),
                                    contentDescription = "Card drawn",
                                    modifier = modifier
                                        .width(120.dp)
                                        .wrapContentHeight()
                                )
                                Column {
                                    Spacer(modifier.height(100.dp))
                                    Text(
                                        text = TheGameHandler.getTheComponents().getComponent(
                                            randomCard.key,
                                            NameComponent::class
                                        ).name
                                    )
                                    Text(
                                        text = TheGameHandler.getTheComponents().getComponent(
                                            randomCard.key,
                                            DescriptionComponent::class
                                        ).description,
                                        softWrap = true,
                                        modifier = modifier.background(color = Color.Yellow),

                                        )


                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CardsOnHand3(
    cardsOnHand: MutableIntState,
    modifier: Modifier,
    cardId: Int,
    cardDisplaySystem: CardDisplaySystem
) {
    Box(modifier = modifier.width(120.dp).height(170.dp)) {
        Box(
            modifier = modifier
                .align(Alignment.BottomStart)
                .background(color = Color.Blue)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = Color.Red) {
                            Text("${cardsOnHand.intValue}")
                        }
                    }
                ) {
                    Card(
                        modifier = modifier.background(color = Color.Green)
                    ) {
                        Column {
                            cardDisplaySystem.EntityDisplay(cardId)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayACard(cardEntity: Int, modifier: Modifier) {
//    Text("moi moi $cardEntity")
    val asddd = CardDisplaySystem(TheGameHandler.getTheComponents())
    Box(modifier = Modifier.fillMaxSize()) {
        asddd.EntityDisplay()
//        DraggableText()
    }
//    DraggableTextLowLevel()
}


@Composable
private fun DraggableTextLowLevel() {
    Box(modifier = Modifier.fillMaxSize()) {
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        Box(
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .size(50.dp)
//                .anchoredDraggable(Unit){
//                    delta ->
//                    offsetX += delta
//                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                    detectTapGestures {
                    }
                }
        )
    }
}


@Composable
private fun DraggableText() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    Text(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(
//                    onTap = { offsetX = 10f },

                    onPress = {
                        val release = try {
                            tryAwaitRelease()
                        } catch (c: CancellationException) {
                            false
                        }
                        if (release) {
                            offsetX = 100f
                        }
                    }
                )
            },
        text = "Drag me!"
    )
}

@Preview(showBackground = true)
@Composable
fun DisplayACardPreview(
    @PreviewParameter(
        CardEntityPreviewParameterProvider::class,
        limit = 1
    ) cardEntity: Int
) {
    DisplayACard(cardEntity = cardEntity, modifier = Modifier)
}


class CardEntityPreviewParameterProvider : PreviewParameterProvider<Int> {
    // TODO here should be some way of actually getting card entities
    override val values = sequenceOf(2, 3, 4)
}
