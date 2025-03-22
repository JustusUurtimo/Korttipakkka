package com.sq.thed_ck_licker.ecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.card.CardClassification
import com.sq.thed_ck_licker.card.CardEffect
import com.sq.thed_ck_licker.card.CardEffectType
import com.sq.thed_ck_licker.card.CardEffectValue
import com.sq.thed_ck_licker.card.CardIdentity
import com.sq.thed_ck_licker.card.Cards


object TheGameHandler {
    val cards = Cards()


    val playerHealth = mutableFloatStateOf(0f)

    /**
     * käytetään placeholderina kun ei ole vielä vedetty kortteja
     * Returns a default card pair with a placeholder card.
     */
    fun getDefaultCardPair(): Pair<CardIdentity, CardEffect> {
        val defaultCardPair = Pair(
            CardIdentity(-1, R.drawable.card_back),
            CardEffect(CardClassification.GOOD, CardEffectType.HEAL, CardEffectValue.HEAL_2)
        )
        return defaultCardPair
    }

}