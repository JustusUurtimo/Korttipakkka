package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.TheGameHandler.cardsSystem
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent

class PlayerSystem {

    fun initPlayer(componentManager: ComponentManager) {
        componentManager.addComponent(getPlayerID(), HealthComponent(0f, 100f))
        componentManager.addComponent(getPlayerID(), ScoreComponent())
        componentManager.addComponent(getPlayerID(), DrawDeckComponent(initPlayerDeck()))
    }

    private fun initPlayerDeck(): List<Int> {
        val playerHealingCards = cardsSystem.initCards(
            R.drawable.heal_10,
            10,
            "This card heals you",
            "Heal",
            listOf(CardTag.CARD),
            cardComponentValue = 10,
            cardComponent = HealthComponent(10f, 0f)
        )
        val playerDamageCards = cardsSystem.initCards(
            5,
            R.drawable.damage_6,
            10,
            "This card deals damage to you",
            "Damage",
            listOf(CardTag.CARD)
        )
        val playerMiscCards = cardsSystem.initCards(
            5,
            R.drawable.shop_coupon,
            10,
            "This card does something",
            "Misc",
            listOf(CardTag.CARD)
        )

        return playerHealingCards + playerDamageCards + playerMiscCards
    }

}