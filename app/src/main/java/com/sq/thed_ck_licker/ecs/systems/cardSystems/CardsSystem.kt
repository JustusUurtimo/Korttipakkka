package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.activate
import com.sq.thed_ck_licker.ecs.components.addEntity
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.discardSystem
import com.sq.thed_ck_licker.helpers.getRandomElement
import kotlin.math.min

class CardsSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {


    companion object {
        val instance: CardsSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardsSystem(ComponentManager.componentManager)
        }
    }


    fun pullRandomCardFromEntityDeck(entityId: Int): Int {
        val drawDeck = entityId get DrawDeckComponent::class
        check(drawDeck.drawCardDeck.isNotEmpty()) { "No cards available" }
        val theCard = drawDeck.drawCardDeck.getRandomElement()
        drawDeck.drawCardDeck.remove(theCard)
        return theCard
    }

    fun activateCard(latestCard: MutableIntState, playerCardCount: MutableIntState) {
        playerCardCount.intValue += 1
        val latestCardId = latestCard.intValue

        try {
            (latestCardId get EffectComponent::class).onPlay.invoke(getPlayerID(), latestCardId)
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No effect component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no effect component"
            )
        }

        try {
            (latestCardId get HealthComponent::class).health.floatValue -= 1f
            Log.i(
                "CardsSystem",
                "Health is now ${(latestCardId get HealthComponent::class).health.floatValue}"
            )
            if ((latestCardId get HealthComponent::class).health.floatValue <= 0) {
                latestCard.intValue = -1
            }
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No health component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no health component"
            )
        }

        try {
            (latestCardId get ActivationCounterComponent::class).activate()
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No actCounter component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no actCounter component"
            )
        }
        discardSystem(ownerId = getPlayerID(), cardId = latestCardId)
        latestCard.intValue = -1
    }

    fun addPassiveScoreGainerToEntity(targetId: Int, pointsPerCard: Int = 3) {

        val gainerEntity = generateEntity()
        val gainerActCounterComp = ActivationCounterComponent()
        val activateAction = { id: Int ->
            val targetScoreComp = id get ScoreComponent::class
            gainerActCounterComp.activate()
            targetScoreComp.score.intValue += pointsPerCard
        }
        gainerEntity add gainerActCounterComp
        gainerEntity add EffectComponent(onTurnStart = activateAction)

        val targetEffectStackComp = (targetId get EffectStackComponent::class)
        targetEffectStackComp addEntity (gainerEntity)  // I think i have gone mad from the power
    }

    fun addLimitedSupplyAutoHealToEntity(targetEntityId: Int, health: Float) {
        val limitedHealEntity = generateEntity()
        val selfHp = HealthComponent(health)
        limitedHealEntity add selfHp
        val selfActCounter = ActivationCounterComponent()
        limitedHealEntity add selfActCounter

        limitedHealEntity add EffectComponent(onTurnStart = { id: Int ->
            val targetHealthComponent = id get HealthComponent::class
            val targetMaxHp = targetHealthComponent.maxHealth.floatValue
            val targetHp = targetHealthComponent.health.floatValue
            if (targetHp < targetMaxHp / 2) {
                val amountToHeal = (targetMaxHp * 0.8f) - targetHp
                val amountOfHealingProvided = min(selfHp.health.floatValue, amountToHeal)
                selfHp.health.floatValue -= amountOfHealingProvided
                targetHealthComponent.health.floatValue += amountOfHealingProvided
            }
            println("My name is Beer Goggles")
            println("I am now at ${selfHp.health.floatValue} health \nand have been activated ${selfActCounter.activations.intValue} times")
        })

        val targetEffectStackComp = (targetEntityId get EffectStackComponent::class)
        targetEffectStackComp addEntity (limitedHealEntity)
    }

}