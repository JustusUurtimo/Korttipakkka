package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager.Companion.componentManager
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler

object TickingSystem {

    /**
     * @param value is the amount that time has moved in milliseconds.
     */
    fun tick(value: Int = 100) {
        val entitiesWithTickComponent: Map<Int, Any> =
            componentManager.getEntitiesWithComponent(TickComponent::class) ?: return
        for (entry in entitiesWithTickComponent) {
            val tickComp = entry.value as TickComponent
            tickComp.currentAmount += value
            val target = entry.key get OwnerComponent::class
            while (tickComp.currentAmount >= tickComp.tickThreshold) {
                tickComp.currentAmount -= tickComp.tickThreshold
                TriggerEffectHandler.handleTriggerEffect(EffectContext(
                    trigger = Trigger.OnTick,
                    source = entry.key,
                    target = target.ownerId,
                ))
            }
        }
        DeathSystem.checkForDeath() //If this becomes performance problem, just make system that can check for only themselves.
    }
}