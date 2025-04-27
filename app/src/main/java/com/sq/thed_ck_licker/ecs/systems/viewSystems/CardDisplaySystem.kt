package com.sq.thed_ck_licker.ecs.systems.viewSystems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.ImageComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.NameComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem.Companion.instance as merchantSystem


class CardDisplaySystem private constructor(private val componentManager: ComponentManager) {

    companion object {
        val instance: CardDisplaySystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardDisplaySystem(ComponentManager.componentManager)
        }
    }

    @Composable
    private fun EntityDisplay(entityId: Int = 1, activateCard: () -> Unit, modifier: Modifier) {
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

    @Composable
    fun CardsOnMerchantHandView(
        merchantId: MutableIntState,
        modifier: Modifier,
        latestCard: MutableIntState,
        playerScore: MutableIntState,
    ) {

        var merchantHand by remember { mutableStateOf(merchantSystem.reRollMerchantHand(merchantId.intValue)) }
        val activeMerchantSummonCard = rememberSaveable {(getPlayerID() get MerchantComponent::class).activeMerchantSummonCard }
        val count = rememberSaveable { merchantSystem.getReRollCount(activeMerchantSummonCard.intValue) }

        fun chooseMerchantCard(cardId: Int) {
            playerScore.intValue -= 100
            latestCard.intValue = cardId
            merchantId.intValue = -1
        }

        fun reRollShop(): () -> Unit = {
            if (count.intValue > 1) {
                playerScore.intValue -= 500
            }
            count.intValue++
            merchantHand = merchantSystem.reRollMerchantHand(merchantId.intValue)
        }


        Box(
            modifier = modifier
                .height(225.dp)
                .background(
                    color = Color.Magenta
                ),
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .background(Color.Magenta)
            ) {
                for (card in merchantHand) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        EntityDisplay(
                            card,
                            { chooseMerchantCard(card) },
                            Modifier.fillMaxSize()
                        )
                    }
                }

            }
            Button(
                modifier = modifier
                    .align(Alignment.BottomCenter),
                onClick = reRollShop()
            ) { Text("Re-roll shop") }
        }
    }


    @Composable
    fun CardsOnPlayerHandView(
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

            EntityDisplay(latestCard.intValue, activateCard, modifier)
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
