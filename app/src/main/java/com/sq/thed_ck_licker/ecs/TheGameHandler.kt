package com.sq.thed_ck_licker.ecs

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.card.Cards
import com.sq.thed_ck_licker.ecs.components.CardClassification
import com.sq.thed_ck_licker.ecs.components.CardEffect
import com.sq.thed_ck_licker.ecs.components.CardEffectType
import com.sq.thed_ck_licker.ecs.components.CardEffectValue
import com.sq.thed_ck_licker.ecs.components.CardIdentity

//TODO apparently this kind a not good...
// If you want I can do new refactor to make it better.
// But I think this is good enough for now.
object TheGameHandler {
    val cards = Cards()


    //There is compelling argument to make special object for the player that collects all the special player methods.
    private val playerHealth = mutableFloatStateOf(0f)

    fun getPlayerHealth(): Float {
        return playerHealth.floatValue
    }


    fun getEntityHealth(entityId: Int): Float {
        //TODO: Implement the id based entity search
        // it will need to check all the entities with health components?
        return playerHealth.floatValue

    }

    fun getEntityHealthM(entityId: Int): MutableFloatState {
        return playerHealth

    }

    // TODO this wont stay here
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