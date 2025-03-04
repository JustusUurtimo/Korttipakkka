package com.sq.thed_ck_licker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.card.CardClass
import com.sq.thed_ck_licker.card.CardData
import com.sq.thed_ck_licker.card.CardDeck
import com.sq.thed_ck_licker.player.HealthBar


@Composable
fun Game(innerPadding: PaddingValues) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    val cardsOnDeck = remember { mutableStateOf(CardData.cards) }
    val cardsOnHand = remember { mutableStateOf(Card) }
    Column(modifier = Modifier.fillMaxWidth()) {


        HealthBar(0f, modifier = Modifier.padding(innerPadding))

        Box(modifier = Modifier.fillMaxSize()) {
            CardDeck(navigationBarPadding, cardsOnDeck.value.size)
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                Column (modifier = Modifier.padding(5.dp)) {
                    CardsOnHand(cardsOnHand)
                    DrawCardButton { DrawCard(cardsOnDeck) }
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
fun DrawCard(cardsOnDeck: MutableState<List<CardClass>>) {

}

@Composable
fun CardsOnHand(cards: ArrayList<CardClass>) {
    Row (Modifier.height(200.dp)) {

    }
}