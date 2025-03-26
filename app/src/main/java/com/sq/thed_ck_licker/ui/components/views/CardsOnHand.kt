package com.sq.thed_ck_licker.ui.components.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.TheGameHandler
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.systems.CardsSystem


@Composable
fun CardsOnHand(
    cardsOnHand: MutableIntState,
    modifier: Modifier,
    latestCardID: Int
) {
    //TODO we should really do "entity carry" it should be just holder class that holds entity id
    // and all its components, it should not be used for data manipulation only to present it in places
    // That way we can carry already fetched entities nicely

    // TODO this really should come here differently..
//    val randomCard = TheGameHandler.getRandomCard()!!.entries.first()
    /*   val randomCard = TheGameHandler.getRandomCard()!!.entries.getRandomElement()
       println("randCard $randomCard")
       val thingsOnIt = TheGameHandler.getComponents(randomCard.key)
       println("thingsOnIt $thingsOnIt") //Maybe we should use logs
       Log.d("CardsOnHand2", "thingsOnIt $thingsOnIt") */

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
                                        cards

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
                                            latestCardID.key,
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


