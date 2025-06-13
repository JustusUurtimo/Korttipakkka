package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.EffectComponent
import com.sq.thed_ck_licker.ecs.components.EffectStackComponent
import com.sq.thed_ck_licker.ecs.components.TargetComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.viewSystems.navigationViews.screens.areRealTimeThingsEnabled

fun onDeathSystem(componentManager: ComponentManager = ComponentManager.componentManager) {
    val deaths = mutableListOf<EntityId>()
    deaths += healthDeath(componentManager)

    if (deaths.isEmpty()) return
    performCleanup(componentManager, deaths)
}

private fun healthDeath(componentManager: ComponentManager): List<EntityId> {
    val dying = componentManager.getEntitiesWithComponent(HealthComponent::class)
        ?: return emptyList()
    val deaths = mutableListOf<EntityId>()
    for (someEntryThingDying in dying) {
        val entityId = someEntryThingDying.key
        val target = try {
            (entityId get TargetComponent::class).target
        } catch (_: IllegalStateException) {
            entityId
        }
        val health = (someEntryThingDying.value as HealthComponent).getHealth()
        if (health <= 0) {
            if (entityId == getPlayerID()) {
                GameEvents.tryEmit(GameEvent.PlayerDied)
                areRealTimeThingsEnabled.value = false
            } else {
                deaths.add(deathHappening(entityId, componentManager, target))
                Log.i(
                    "Health Death",
                    "Entity #${entityId} is dead now "
                )
            }
        }
    }
    return deaths

}

private fun deathHappening(
    entityId: EntityId,
    componentManager: ComponentManager,
    targetId: EntityId
): EntityId {
    val target = try {
        (entityId get TargetComponent::class).target
    } catch (_: IllegalStateException) {
        targetId
    }
    try {
        (entityId get EffectComponent::class).onDeath.action(target)
    } catch (_: IllegalStateException) {
        Log.i("Death Happening", "No cool death for you, mate. $entityId ")
    }
    componentManager.removeEntity(entityId)
    return entityId
}


/**
 * This one is not nice...
 * Probably needs refactor to some observer/event-based/subscriber pattern
 */
fun performCleanup(componentManager: ComponentManager, deaths: List<EntityId>) {
    val toClean = componentManager.getEntitiesWithComponent(EffectStackComponent::class) ?: return

    for (entityAndComp in toClean) {
        val component = entityAndComp.value as EffectStackComponent
        component.removeEntities(deaths)
    }

    val toClean2 = componentManager.getEntitiesWithComponent(DrawDeckComponent::class) ?: return

    for (entityAndComp in toClean2) {
        val component = entityAndComp.value as DrawDeckComponent
        component.removeCards(deaths)
    }

    val toClean3 = componentManager.getEntitiesWithComponent(DiscardDeckComponent::class) ?: return

    for (entityAndComp in toClean3) {
        val component = entityAndComp.value as DiscardDeckComponent
        component.removeCards(deaths)
    }

}