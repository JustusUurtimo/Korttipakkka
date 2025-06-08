package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager.Companion.componentManager

class TickingSystem {

    /**
     * @param value is the amount that time has moved in milliseconds.
     */
    fun tick(value: Int = 100) {
        val entitiesWithTickComponent: Map<Int, Any> =
            componentManager.getEntitiesWithComponent(TickComponent::class) ?: return
        for (entry in entitiesWithTickComponent) {
            val tickComp = entry.value as TickComponent
            tickComp.currentAmount += value
            while (tickComp.currentAmount >= tickComp.tickThreshold) {
                tickComp.tickAction.action.invoke(entry.key)
                tickComp.currentAmount -= tickComp.tickThreshold
            }
        }
    }
}