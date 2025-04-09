package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.TheGameHandler.cardsSystem
import com.sq.thed_ck_licker.ecs.add
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.get

class PlayerSystem(private val componentManager: ComponentManager) {

    fun initPlayer() {
        componentManager.addComponent(getPlayerID(), HealthComponent(100f, 100f))
        componentManager.addComponent(getPlayerID(), ScoreComponent())
        componentManager.addComponent(getPlayerID(), DrawDeckComponent(initPlayerDeck()))
        getPlayerID() add EffectStackComponent()
    }

    private fun initPlayerDeck(): List<Int> {
        val playerHealingCards = cardsSystem.initCards(
            5,
            R.drawable.heal_10,
            "This card heals you",
            "Heal",
            listOf(CardTag.CARD),
            cardComponent = HealthComponent(5f, 0f)
        )
        val playerDamageCards = cardsSystem.initCards(
            5,
            R.drawable.damage_6,
            "This card deals damage to you",
            "Damage",
            listOf(CardTag.CARD),
            cardComponent = HealthComponent(-5f, 0f)
        )
        val playerMiscCards = cardsSystem.initCards(
            5,
            R.drawable.shop_coupon,
            "This card does something",
            "Misc",
            listOf(CardTag.CARD),
            cardComponent = ScoreComponent(10)
        )

        val defaultCards = cardsSystem.addDefaultCards(10)

        val deactivationCards = cardsSystem.addDeactivationTestCards(2)

        val trapCards = cardsSystem.addTrapTestCard()

        val scoreGainerCards = cardsSystem.addScoreGainerTestCard()

        // TODO: they are spaced so i can easilly comment then in and out, same for the empty lists
        //  So the real to do is to make more testable code...
        return emptyList<Int>() +
                playerHealingCards +
                playerDamageCards +
                playerMiscCards +
                defaultCards +
                deactivationCards +
                trapCards +
                scoreGainerCards +
                emptyList<Int>()
    }

    fun getPlayerHealthM(): MutableFloatState {
        return (getPlayerID() get HealthComponent::class).health
    }

    fun getPlayerScoreM(): MutableIntState {
        return (getPlayerID() get ScoreComponent::class).score
    }
}