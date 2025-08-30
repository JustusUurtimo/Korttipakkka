package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.OnCreation
import com.sq.thed_ck_licker.ecs.components.effectthing.OnDeath
import com.sq.thed_ck_licker.ecs.components.effectthing.OnTick
import com.sq.thed_ck_licker.ecs.components.effectthing.OnTurnStart
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeSelfDamage
import com.sq.thed_ck_licker.ecs.components.effectthing.healthEffects.HealOnUnderThreshold
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.AddMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.multiplierEffects.RemoveMultiplier
import com.sq.thed_ck_licker.ecs.components.effectthing.scoreEffects.GainScore
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler

object CardCreationHelperSystems2 {

    fun addPassiveScoreGainerToEntity(targetId: Int, pointsPerCard: Int = 3): EntityId {
        val gainerEntity = generateEntity()
        val scoreComp = ScoreComponent(pointsPerCard)
        gainerEntity add scoreComp
        gainerEntity add TriggeredEffectsComponent(
            OnTurnStart,
            GainScore(scoreComp.getScoreF())
        )
        gainerEntity add OwnerComponent(targetId)

        return gainerEntity
    }

    fun addLimitedSupplyAutoHealToEntity(targetId: EntityId, health: Float, threshold: Float = 0.5f): EntityId {
        val limitedHealEntity = generateEntity()
        limitedHealEntity add HealthComponent(health)
        limitedHealEntity add TriggeredEffectsComponent(
            OnTick, HealOnUnderThreshold(health, threshold)
        )
        limitedHealEntity add OwnerComponent(targetId)
        return limitedHealEntity
    }


    fun addTemporaryMultiplierTo(
        targetEntityId: EntityId,
        health: Float = 28f,
        multiplier: Float = 2.8f
    ): EntityId {
        val limitedMultiEntity = generateEntity()
        limitedMultiEntity add  HealthComponent(health)
        limitedMultiEntity add OwnerComponent(targetEntityId)

        limitedMultiEntity add TriggeredEffectsComponent(
            mutableMapOf(
                OnCreation to mutableListOf(
                    AddMultiplier(multiplier)
                ),
                OnTurnStart to mutableListOf(
                    TakeSelfDamage(1f)
                ),
                OnDeath to mutableListOf(
                    RemoveMultiplier(multiplier)
                )
            )
        )

        TriggerEffectHandler.handleTriggerEffect(EffectContext(
            trigger = OnCreation,
            source = limitedMultiEntity,
            target = targetEntityId
        ))
        return limitedMultiEntity
    }
}