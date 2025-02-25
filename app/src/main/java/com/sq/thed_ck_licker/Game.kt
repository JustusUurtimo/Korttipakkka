package com.sq.thed_ck_licker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sq.thed_ck_licker.card.CardData
import com.sq.thed_ck_licker.card.CardDeck
import com.sq.thed_ck_licker.player.HealthBar


@Composable
fun Game(innerPadding: PaddingValues) {
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues()
    Column(modifier = Modifier.fillMaxWidth()) {
        val cardsOnDeck = remember { mutableStateOf(CardData.cards) }

        HealthBar(0f, modifier = Modifier.padding(innerPadding))

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CardDeck(navigationBarPadding, cardsOnDeck.value.size)
        }


    }
}