package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.R.attr.targetId
import android.util.Log
import com.sq.thed_ck_licker.ecs.components.ActivationCounterComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.generateEntity
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler
import com.sq.thed_ck_licker.helpers.DescribedEffect
import javax.inject.Inject

class CardCreationHelperSystems @Inject constructor() {

    fun addPassiveScoreGainerToEntity(targetId: Int, pointsPerCard: Int = 3): EntityId {
        val gainerEntity = generateEntity()
        val scoreComp = ScoreComponent(pointsPerCard)
        gainerEntity add scoreComp
        gainerEntity add TriggeredEffectsComponent(
            Trigger.OnTurnStart,
            Effect.GainScore(scoreComp.getScore())
        )
        gainerEntity add OwnerComponent(targetId)

        return gainerEntity
    }

    fun addLimitedSupplyAutoHealToEntity(targetId: EntityId, health: Float, threshold: Float = 0.5f): EntityId {
        val limitedHealEntity = generateEntity()
        val selfHp = HealthComponent(health)
        limitedHealEntity add selfHp
        limitedHealEntity add TriggeredEffectsComponent(
            Trigger.OnTurnStart, Effect.HealOnUnderThreshold(threshold, health)
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
        val selfHp = HealthComponent(health)
        limitedMultiEntity add selfHp
        limitedMultiEntity add OwnerComponent(targetEntityId)

        limitedMultiEntity add TriggeredEffectsComponent(
            mutableMapOf(
                Trigger.OnCreation to mutableListOf(
                    Effect.AddMultiplier(multiplier)
                ),
                Trigger.OnTurnStart to mutableListOf(
                    Effect.TakeSelfDamage(1f)
                ),
                Trigger.OnDeath to mutableListOf(
                    Effect.RemoveMultiplier(multiplier)
                )
            )
        )

        TriggerEffectHandler.handleTriggerEffect(EffectContext(
            trigger = Trigger.OnCreation,
            source = limitedMultiEntity,
            target = targetEntityId
        ))
        return limitedMultiEntity
    }
}