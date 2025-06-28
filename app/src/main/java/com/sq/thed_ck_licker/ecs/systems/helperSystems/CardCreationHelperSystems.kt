package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.helpers.DescribedEffect
import javax.inject.Inject
import kotlin.math.min

class CardCreationHelperSystems @Inject constructor() {

    fun addPassiveScoreGainerToEntity(targetId: Int, pointsPerCard: Int = 3) {

        val gainerEntity = generateEntity()
        val activationCounter = ActivationCounterComponent()
        val activateAction: (Int) -> Unit = { id: Int ->
            val targetScoreComp = id get ScoreComponent::class
            activationCounter.activate()
            targetScoreComp.addScore(pointsPerCard)
        }
        val activationEffect =
            DescribedEffect(activateAction) { "Gain $pointsPerCard points per card played" }
        gainerEntity add activationCounter
        gainerEntity add EffectComponent(onTurnStart = activationEffect)

        val targetEffectStackComp = (targetId get EffectStackComponent::class)
        targetEffectStackComp addEntity (gainerEntity)  // I think i have gone mad from the power
    }

    fun addLimitedSupplyAutoHealToEntity(targetEntityId: Int, health: Float) {
        val limitedHealEntity = generateEntity()
        val selfHp = HealthComponent(health)
        limitedHealEntity add selfHp
        val selfActCounter = ActivationCounterComponent()
        limitedHealEntity add selfActCounter

        val healThreshold = 0.5f
        var healedAmount = 0f
        val onTurnStart: (Int) -> Unit = { id: Int ->
            val targetHealthComponent = id get HealthComponent::class
            val targetMaxHp = targetHealthComponent.getMaxHealth()
            val targetHp = targetHealthComponent.getHealth()
            if (targetHp < targetMaxHp * healThreshold) {
                val amountToHeal = (targetMaxHp * 0.8f) - targetHp
                val amountOfHealingProvided = min(selfHp.getHealth(), amountToHeal)
                healedAmount = amountOfHealingProvided
                selfHp.damage(amountOfHealingProvided)
                targetHealthComponent.heal(amountOfHealingProvided)
            }
            Log.i("CardsSystem", "My name is Beer Goggles")
            Log.i(
                "CardsSystem",
                "I am now at ${selfHp.getHealth()} health \nand have been activated ${selfActCounter.getActivations()} times"
            )
        }
        val activationEffect =
            DescribedEffect(onTurnStart) { "If you are under ${healThreshold * 100}% health, heal $healedAmount" }
        limitedHealEntity add EffectComponent(onTurnStart = activationEffect)

        val targetEffectStackComp = (targetEntityId get EffectStackComponent::class)
        targetEffectStackComp addEntity (limitedHealEntity)
    }

    fun addTemporaryMultiplierTo(
        targetEntityId: Int,
        health: Float = 10f,
        multiplier: Float = 2.8f
    ) {
        val limitedMultiEntity = generateEntity()
        val selfHp = HealthComponent(health)
        limitedMultiEntity add selfHp
        limitedMultiEntity add ActivationCounterComponent()


        try {
            val targetMultiComp = targetEntityId get MultiplierComponent::class
            targetMultiComp.timesMultiplier(multiplier)
        } catch (_: IllegalStateException) {
            Log.e("CardsSystem", "Target entity has no multiplier component")
        }
        val damage = 1f
        val onTurnStart = { _: Int -> selfHp.damage(damage) }
        val activationEffect =
            DescribedEffect(onTurnStart) { "Take $damage damage" }

        val onDeath = { targetId: Int ->
            val targetMultiComp = targetId get MultiplierComponent::class
            targetMultiComp.removeMultiplier(multiplier)
        }

        val onDeathEffect =
            DescribedEffect(onDeath) { "Removes the $multiplier multiplier" }
        limitedMultiEntity add EffectComponent(
            onTurnStart = activationEffect,
            onDeath = onDeathEffect
        )

    }
}