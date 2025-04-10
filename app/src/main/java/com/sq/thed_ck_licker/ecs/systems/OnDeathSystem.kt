package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.components.DurationComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.get

fun onDeathSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    healthDeath(componentManager)
    durationDeath(componentManager)
    return
}

private fun healthDeath(componentManager: ComponentManager) {
    val dying = componentManager.getEntitiesWithComponent(HealthComponent::class)
    if (dying == null) return
    for (entity in dying) {

//        //These two lines are here to protect the old time cards from being removed.
//        val comps = componentManager.getAllComponentsOfEntity(entity.key)
//        if (!comps.hasComponent<EffectComponent>()) continue

        val health = (entity.value as HealthComponent).health.floatValue
        if (health <= 0) {
            deathHappening(entity, componentManager)
            println("Death happened, such shame")
            println("Entity #${entity.key} is dead now")
        }
    }
}

private fun durationDeath(componentManager: ComponentManager) {
    val dying = componentManager.getEntitiesWithComponent(DurationComponent::class)
    if (dying == null) return
    for (entity in dying) {
        val duraComp = (entity.value as DurationComponent)
        if (duraComp.duration <= 0 && duraComp.infinite) {
            deathHappening(entity, componentManager)
        }
    }
}

private fun deathHappening(
    entity: Map.Entry<Int, Any>,
    componentManager: ComponentManager
) {
    try {
        // TODO: onDeaths can currently target only the player, not other entities
        //  Gotta think this one for a moment, maybe some targetComponent or something.
        (entity.key get EffectComponent::class).onDeath.invoke(getPlayerID())
    } catch (_: Exception) {
        println("No cool death for you, mate. ${entity.key} ")
    }
    componentManager.removeEntity(entity.key)
}