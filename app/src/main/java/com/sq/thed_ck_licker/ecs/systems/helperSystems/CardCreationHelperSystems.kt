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
import javax.inject.Inject
import kotlin.math.min

class CardCreationHelperSystems @Inject constructor() {

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
                selfHp.damage(amountOfHealingProvided)
                targetHealthComponent.heal(amountOfHealingProvided)
            }
            println("My name is Beer Goggles")
            println("I am now at ${selfHp.getHealth()} health \nand have been activated ${selfActCounter.getActivations()} times")
        })

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

        limitedMultiEntity add EffectComponent(
            onTurnStart = { _: Int ->
                selfHp.damage(1f)
            },
            onDeath = { targetId: Int ->
                val targetMultiComp = targetId get MultiplierComponent::class
                targetMultiComp.removeMultiplier(multiplier)
            })
    }
}