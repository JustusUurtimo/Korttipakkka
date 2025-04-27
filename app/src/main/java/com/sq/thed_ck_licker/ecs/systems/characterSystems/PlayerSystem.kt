package com.sq.thed_ck_licker.ecs.systems.characterSystems

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getRegularMerchantID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MerchantComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.CardCreationSystem.Companion.instance as cardCreationSystem


class PlayerSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {
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
        getPlayerID() add DiscardDeckComponent()
    }

    private fun initPlayerDeck(): List<Int> {

        val playerHealingCards = cardCreationSystem.addHealingCards(1)
        val playerDamageCards = cardCreationSystem.addDamageCards(5)
        val defaultCards = cardCreationSystem.addBreakingDefaultCards(10)
        val deactivationCards = cardCreationSystem.addDeactivationTestCards(2)
        val trapCards = cardCreationSystem.addTrapTestCards()
        val scoreGainerCards = cardCreationSystem.addScoreGainerTestCards()
        val beerGogglesCards = cardCreationSystem.addBeerGogglesTestCards()
        val maxHpCards = cardCreationSystem.addMaxHpTrapCards()
        val merchantCards = cardCreationSystem.addMerchantCards(5, getRegularMerchantID())
        val basicScoreCards = cardCreationSystem.addBasicScoreCards(2)

        return emptyList<Int>() +
                playerHealingCards +
                playerDamageCards +
                defaultCards +
                deactivationCards +
                trapCards +
                scoreGainerCards +
                beerGogglesCards +
                maxHpCards +
                merchantCards +
                basicScoreCards +
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