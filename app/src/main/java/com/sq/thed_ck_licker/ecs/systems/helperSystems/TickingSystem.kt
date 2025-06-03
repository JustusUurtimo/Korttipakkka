package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.TickComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager.Companion.componentManager
import com.sq.thed_ck_licker.ecs.managers.EntityManager
import com.sq.thed_ck_licker.ecs.managers.get

class TickingSystem {

    /**
     * @param value is the ms amount that time has moved.
     */
    fun tick(value: Int = 100) {
        val tickable: Map<Int, Any> =
            componentManager.getEntitiesWithComponent(TickComponent::class) ?: return

        for (tickableEntry in tickable) {
            val tickComp = tickableEntry.value as TickComponent
            tickComp.currentAmount += value
            while (tickComp.currentAmount >= tickComp.tickThreshold) {
                tickComp.tickAction.invoke(tickableEntry.key)
                tickComp.currentAmount -= tickComp.tickThreshold
            }
        }
    }

    fun proofOfConcept(){
        val player = EntityManager.getPlayerID()
        val playerHealth = (player get HealthComponent::class)
        playerHealth.damage(1f)
        if (playerHealth.getHealth() <= 0) {
            onDeathSystem()
        }
    }
}