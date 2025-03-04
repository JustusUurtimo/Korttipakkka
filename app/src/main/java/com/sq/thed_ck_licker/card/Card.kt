package com.sq.thed_ck_licker.card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sq.thed_ck_licker.R

data class CardClass(
    val id: Int,
    @DrawableRes val cardImage: Int,
)

object CardData {
    val cards = listOf(
        CardClass(
            id = 1,
            cardImage = R.drawable.kunkku
        ),
        CardClass(
            id = 2,
            cardImage = R.drawable.jatka
        ),
        CardClass(
            id = 3,
            cardImage = R.drawable.card_2
        ),
        CardClass(
            id = 4,
            cardImage = R.drawable.card_3
        ),
        CardClass(
            id = 5,
            cardImage = R.drawable.card_4
        )
    )
}

@Composable
fun Card(
    card: CardClass,
    index: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = card.cardImage),
        contentDescription = "Card ${card.id}",
        modifier = modifier
            .width(120.dp)
            .wrapContentHeight()
    )
}