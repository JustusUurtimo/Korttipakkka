package com.sq.thed_ck_licker.card

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.sq.thed_ck_licker.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardClass(
    val id: Int,
    @DrawableRes val cardImage: Int,
    val damageValue: Float
) : Parcelable

object CardData {
    var cards = ArrayDeque(
        listOf(
            CardClass(
                id = 1,
                cardImage = R.drawable.kunkku,
                damageValue = 5f
            ),
            CardClass(
                id = 2,
                cardImage = R.drawable.jatka,
                damageValue = -5f
            ),
            CardClass(
                id = 3,
                cardImage = R.drawable.card_2,
                damageValue = 2f
            ),
            CardClass(
                id = 4,
                cardImage = R.drawable.card_3,
                damageValue = 10f
            ),
            CardClass(
                id = 5,
                cardImage = R.drawable.card_4,
                damageValue = -6f
            )
        )
    )
}