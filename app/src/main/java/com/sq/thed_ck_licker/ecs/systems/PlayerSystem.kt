package com.sq.thed_ck_licker.ecs.systems

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.R
import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.add
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.get
import com.sq.thed_ck_licker.ecs.systems.CardsSystem.Companion.instance as cardsSystem

class PlayerSystem private constructor(private val componentManager: ComponentManager) {
    companion object {
        val instance: PlayerSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            PlayerSystem(ComponentManager.componentManager)
        }
    }

    fun initPlayer() {
        getPlayerID() add HealthComponent(100f)
        getPlayerID() add ScoreComponent()
        getPlayerID() add MerchantComponent()
        getPlayerID() add DrawDeckComponent(initPlayerDeck() as MutableList<Int>)
        getPlayerID() add EffectStackComponent()
    }

    private fun initPlayerDeck(): List<Int> {
        val onActivationScore = { id: Int ->
            (id get ScoreComponent::class).score.intValue += 10
        }
        val onActivationHeal = { id: Int ->
            (id get HealthComponent::class).health.floatValue += 5f
        }
        val onActivationTakeDamage = { id: Int ->
            (id get HealthComponent::class).health.floatValue -= 5f
        }

        val playerHealingCards = cardsSystem.initCards(
            5,
            R.drawable.heal_10,
            "This card heals you",
            "Heal",
            listOf(CardTag.CARD),
            cardComponent = EffectComponent(onPlay = onActivationHeal)
        )
        val playerDamageCards = cardsSystem.initCards(
            5,
            R.drawable.damage_6,
            "This card deals damage to you",
            "Damage",
            listOf(CardTag.CARD),
            cardComponent = EffectComponent(onPlay = onActivationTakeDamage)
        )
        val playerMiscCards = cardsSystem.initCards(
            5,
            R.drawable.shop_coupon,
            "This card does something",
            "Misc",
            listOf(CardTag.CARD),
            cardComponent = EffectComponent(onPlay = onActivationScore)
        )
        val defaultCards = cardsSystem.addBreakingDefaultCards(10)
        val deactivationCards = cardsSystem.addDeactivationTestCards(2)
        val trapCards = cardsSystem.addTrapTestCard()
        val scoreGainerCards = cardsSystem.addScoreGainerTestCard()
        val beerGogglesCards = cardsSystem.addBeerGogglesTestCard()
        val maxHpCards = cardsSystem.addMaxHpTrapCards()
        val merchantCards = cardsSystem.addMerchantCards(5, getRegularMerchantID())

        return emptyList<Int>() +
                playerHealingCards +
                playerDamageCards +
                playerMiscCards +
                defaultCards +
                deactivationCards +
                trapCards +
                scoreGainerCards +
                beerGogglesCards +
                maxHpCards +
                merchantCards +
                emptyList<Int>()
    }

    fun getPlayerHealthM(): MutableFloatState {
        return (getPlayerID() get HealthComponent::class).health
    }

    fun getPlayerScoreM(): MutableIntState {
        return (getPlayerID() get ScoreComponent::class).score
    }

    fun getPlayerMaxHealthM(): MutableFloatState {
        return (getPlayerID() get HealthComponent::class).maxHealth
    }
    fun getMerchant(): MutableIntState {
        return (getPlayerID() get MerchantComponent::class).merchantId
    }
}