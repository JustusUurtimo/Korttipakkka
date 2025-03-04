package com.sq.thed_ck_licker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.card.CardClass
import com.sq.thed_ck_licker.card.CardData
import com.sq.thed_ck_licker.card.CardDeck
import com.sq.thed_ck_licker.player.HealthBar


@Composable
fun Game(innerPadding: PaddingValues) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val cardsOnDeck = rememberSaveable() { CardData.cards }
    val cardsOnHand = rememberSaveable() { mutableIntStateOf(0) }
    val latestCard = rememberSaveable() { mutableIntStateOf(0) }

    val modifier = Modifier

    Column(modifier.fillMaxWidth()) {
        HealthBar(0f, modifier.padding(innerPadding))

        Box(modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding, cardsOnDeck.size)
            Box(modifier.align(Alignment.BottomCenter)) {
                Column(modifier.padding(5.dp)) {
                    CardsOnHand(cardsOnHand, modifier)
                    VerticalDivider()
                    DrawCardButton { DrawCard(cardsOnDeck, cardsOnHand, modifier, latestCard) }

                }

            }
        }

    }
}

@Composable
fun DrawCardButton(click: @Composable () -> Unit) {
    Button(onClick = { click }) { Text("Draw") }
}

@Composable
fun DrawCard(cardsOnDeck: ArrayDeque<CardClass>, cardsOnHand: MutableIntState, modifier: Modifier, latestCard: MutableIntState) {

    Column (modifier = modifier.padding(16.dp)) {
        Text("draw a card")
        Button(onClick = {
            if(cardsOnDeck.isNotEmpty()) {
                cardsOnHand.intValue++
                latestCard.intValue = cardsOnDeck.removeLast().id
            }
        }) { }
    }
}

@Composable
fun CardsOnHand(cardsOnHand: MutableIntState, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BadgedBox(
            badge = {
                Badge(containerColor = Color.LightGray) {
                    Text("$cardsOnHand")
                }
            }
        ) {
            Card {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.kunkku),
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