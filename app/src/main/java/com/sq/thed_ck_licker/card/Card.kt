package com.sq.thed_ck_licker.card

import androidx.annotation.DrawableRes
import com.sq.thed_ck_licker.R

data class CardClass(
    val id: Int,
    @DrawableRes val cardImage: Int,
)

object CardData {
    var cards = ArrayDeque(
        listOf(
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
    )
}