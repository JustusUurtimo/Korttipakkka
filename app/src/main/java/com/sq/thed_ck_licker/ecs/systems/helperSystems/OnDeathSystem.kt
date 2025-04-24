package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.EntityId
import com.sq.thed_ck_licker.ecs.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.DurationComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.get

fun onDeathSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val deaths = mutableListOf<EntityId>()
    deaths += healthDeath(componentManager)
    deaths += durationDeath(componentManager)

    if (deaths.isEmpty()) return
    performCleanup(componentManager, deaths)
}

private fun healthDeath(componentManager: ComponentManager): List<EntityId> {
    val dying = componentManager.getEntitiesWithComponent(HealthComponent::class)
        ?: return emptyList()
    val deaths = mutableListOf<EntityId>()
    for (entity in dying) {

        val health = (entity.value as HealthComponent).health
        if (health <= 0) {
            deaths.add(deathHappening(entity, componentManager))
            println("Death happened, such shame")
            println("Entity #${entity.key} is dead now")
        }
    }
    return deaths
}

private fun durationDeath(componentManager: ComponentManager): List<EntityId> {
    val dying = componentManager.getEntitiesWithComponent(DurationComponent::class)
        ?: return emptyList()
    val deaths = mutableListOf<EntityId>()
    for (entity in dying) {
        val duraComp = (entity.value as DurationComponent)
        if (duraComp.duration <= 0 && duraComp.infinite) {
            deaths.add(deathHappening(entity, componentManager))
        }
    }
    return deaths
}

private fun deathHappening(
    entity: Map.Entry<Int, Any>,
    componentManager: ComponentManager
): EntityId {
    try {
        // TODO: onDeaths can currently target only the player, not other entities
        //  Gotta think this one for a moment, maybe some targetComponent or something.
        (entity.key get EffectComponent::class).onDeath.invoke(getPlayerID())
    } catch (_: IllegalStateException) {
        println("No cool death for you, mate. ${entity.key} ")
    }
    componentManager.removeEntity(entity.key)
    return entity.key
}


/**
 * This one is not nice...
 * Probably needs refactor to some observer/event-based/subscriber pattern
 */
fun performCleanup(componentManager: ComponentManager, deaths: List<EntityId>) {
    val toClean = componentManager.getEntitiesWithComponent(EffectStackComponent::class) ?: return

    for (entityAndComp in toClean) {
        val component = entityAndComp.value as EffectStackComponent
        component.effectEntities.removeAll(deaths)
    }

    val toClean2 = componentManager.getEntitiesWithComponent(DrawDeckComponent::class) ?: return

    for (entityAndComp in toClean2) {
        val component = entityAndComp.value as DrawDeckComponent
        component.drawCardDeck.removeAll(deaths)
    }

    val toClean3 = componentManager.getEntitiesWithComponent(DiscardDeckComponent::class) ?: return

    for (entityAndComp in toClean3) {
        val component = entityAndComp.value as DiscardDeckComponent
        component.discardDeck.removeAll(deaths)
    }

}