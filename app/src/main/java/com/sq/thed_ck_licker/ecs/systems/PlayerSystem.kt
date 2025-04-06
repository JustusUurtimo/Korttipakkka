package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.TheGameHandler.cardsSystem
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent

class PlayerSystem(private val componentManager: ComponentManager) {

    fun initPlayer() {
        componentManager.addComponent(getPlayerID(), HealthComponent(0f, 100f))
        componentManager.addComponent(getPlayerID(), ScoreComponent())
        componentManager.addComponent(getPlayerID(), DrawDeckComponent(initPlayerDeck()))
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

        return playerHealingCards + playerDamageCards + playerMiscCards
    }

}