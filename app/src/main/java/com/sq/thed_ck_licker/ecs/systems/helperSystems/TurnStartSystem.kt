package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.OnTurnStart
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler.handleTriggerEffect

object TurnStartSystem {
    fun onTurnStart(componentManager: ComponentManager = ComponentManager.componentManager) {
        val triggeredEffectEntities =
            componentManager.getEntitiesWithComponent(TriggeredEffectsComponent::class)
        if (triggeredEffectEntities == null) return

        for ((entityId, component) in triggeredEffectEntities) {
            val effects = component.effectsByTrigger[OnTurnStart]
            if (effects == null) continue
            val owner = (entityId get OwnerComponent::class).ownerId
            val context = EffectContext(
                trigger = OnTurnStart,
                source = entityId,
                target = owner
            )
            handleTriggerEffect(context)
        }
    }
}