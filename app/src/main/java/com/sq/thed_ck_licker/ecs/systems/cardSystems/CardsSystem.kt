package com.sq.thed_ck_licker.ecs.systems.cardSystems

import android.util.Log
import androidx.compose.runtime.MutableIntState
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.helperSystems.discardSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onDeathSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.onTurnStartEffectStackSystem
import com.sq.thed_ck_licker.helpers.getRandomElement
import javax.inject.Inject
import kotlin.math.min

class CardsSystem @Inject constructor() {


    fun pullRandomCardFromEntityDeck(entityId: Int): Int {
        val drawDeck = (entityId get DrawDeckComponent::class).getDrawCardDeck()
        check(drawDeck.isNotEmpty()) { "No cards available" }
        val theCard = drawDeck.getRandomElement()
        drawDeck.remove(theCard)
        return theCard
    }

    fun cardActivation(
        latestCard: MutableIntState,
        playerCardCount: MutableIntState
    ) {
        onTurnStartEffectStackSystem()
        activateCard(latestCard, playerCardCount)
        onDeathSystem()
    }

    private fun activateCard(latestCard: MutableIntState, playerCardCount: MutableIntState) {
        playerCardCount.intValue += 1
        val latestCardId = latestCard.intValue
        var latestCardHp : HealthComponent? = null

        try {
            latestCardHp = (latestCardId get HealthComponent::class)
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No health component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no health component"
            )
        }


        try {
            (latestCardId get EffectComponent::class).onPlay.invoke(getPlayerID(), latestCardId)
        } catch (_: IllegalStateException) {
            Log.i(
                "CardsSystem",
                "No effect component found for activation \n" +
                        "Yeah yeah, we get it, you are so cool there was no effect component"
            )
        }

        latestCardHp?.apply {
            damage(1f, latestCardId)
            Log.i(
                "CardsSystem",
                "Health is now ${latestCardHp.getHealth()}"
            )
            if (latestCardHp.getHealth() <= 0) {
                latestCard.intValue = -1
            }
        } ?: Log.i("CardsSystem", "No health component found for activation")

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
            targetScoreComp.addScore(pointsPerCard)
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
            val targetMaxHp = targetHealthComponent.getMaxHealth()
            val targetHp = targetHealthComponent.getHealth()
            if (targetHp < targetMaxHp / 2) {
                val amountToHeal = (targetMaxHp * 0.8f) - targetHp
                val amountOfHealingProvided = min(selfHp.getHealth(), amountToHeal)
                selfHp.damage(amountOfHealingProvided, limitedHealEntity)
                targetHealthComponent.heal(amountOfHealingProvided)
            }
            println("My name is Beer Goggles")
            println("I am now at ${selfHp.getHealth()} health \nand have been activated ${selfActCounter.getActivations()} times")
        })

        val targetEffectStackComp = (targetEntityId get EffectStackComponent::class)
        targetEffectStackComp addEntity (limitedHealEntity)
    }

}