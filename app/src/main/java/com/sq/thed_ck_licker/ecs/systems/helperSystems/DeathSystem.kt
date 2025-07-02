package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.DiscardDeckComponent
import com.sq.thed_ck_licker.ecs.components.DrawDeckComponent
import com.sq.thed_ck_licker.ecs.components.OwnerComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.Effect
import com.sq.thed_ck_licker.ecs.components.effectthing.EffectContext
import com.sq.thed_ck_licker.ecs.components.effectthing.Trigger
import com.sq.thed_ck_licker.ecs.components.effectthing.TriggeredEffectsComponent
import com.sq.thed_ck_licker.ecs.components.effectthing.miscEffects.CoActivation
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.LatestCardComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.EntityManager.getPlayerID
import com.sq.thed_ck_licker.ecs.managers.GameEvent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get
import com.sq.thed_ck_licker.ecs.systems.cardSystems.TriggerEffectHandler
import com.sq.thed_ck_licker.helpers.Settings.isRealTimePlayerDamageEnabled
import kotlin.reflect.KClass

object DeathSystem {
    fun checkForDeath(componentManager: ComponentManager = ComponentManager.componentManager) {
        val deaths = mutableListOf<EntityId>()
        deaths += healthDeath(componentManager)
        if (deaths.isEmpty()) return
        performCleanup(componentManager, deaths)
    }

    private fun healthDeath(componentManager: ComponentManager): List<EntityId> {
        val dying = componentManager.getEntitiesWithComponent(HealthComponent::class)
            ?: return emptyList()
        val deaths = mutableListOf<EntityId>()
        for (entity in dying) {
            val health = (entity.value).getHealth()
            if (health <= 0) {
                if (entity.key == getPlayerID()) {
                    GameEvents.tryEmit(GameEvent.PlayerDied)
                    isRealTimePlayerDamageEnabled.value = false
                } else {
                    deaths.add(deathHappening(entity, componentManager))
                    Log.i(
                        "Health Death",
                        "Death happened, such shame\nEntity #${entity.key} is dead now "
                    )
                }
            }
        }
        return deaths

    }

    private fun deathHappening(
        dyingEntityEntry: Map.Entry<Int, Any>,
        componentManager: ComponentManager
    ): EntityId {
        val entityId = dyingEntityEntry.key
        try {
            val owner = try {
                (entityId get OwnerComponent::class).ownerId
            } catch (_: IllegalStateException) {
                entityId
            }
            TriggerEffectHandler.handleTriggerEffect(
                EffectContext(
                    trigger = Trigger.OnDeath,
                    source = entityId,
                    target = owner
                )
            )
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
    private fun performCleanup(componentManager: ComponentManager, deaths: List<EntityId>) {

        val toClean2 = componentManager.getEntitiesWithComponent(DrawDeckComponent::class) ?: return

        for (entityAndComp in toClean2) {
            val component = entityAndComp.value
            component.removeCards(deaths)
        }

        val toClean3 =
            componentManager.getEntitiesWithComponent(DiscardDeckComponent::class) ?: return

        for (entityAndComp in toClean3) {
            val component = entityAndComp.value
            component.removeCards(deaths)
        }

        val toClean4 =
            componentManager.getEntitiesWithComponent(LatestCardComponent::class) ?: return
        for (entityAndComp in toClean4) {
            val component = entityAndComp.value
            deaths.forEach {
                if (component.getLatestCard() == it) {
                    component.setLatestCard(-1)
                }
            }
        }


        /*
         * This here...
         * It most certainly does not work...
         * Well let me be more specific:
         * At least the descriptions are not updated,
         * Even tho the tested effect seems to be gone.
         */
        val toClean5 =
            componentManager.getEntitiesWithComponent(TriggeredEffectsComponent::class) ?: return

        for (entityAndComp in toClean5) {
            val component = entityAndComp.value

            @Suppress("UNCHECKED_CAST")
            val asd = component.hasEffect(CoActivation::class as KClass<Effect>)
            if (asd == null) return
            val effects = component.findEffect(CoActivation::class)
            for (effect in effects) {
                if (deaths.contains(effect.newSource)) {
                    component.removeEffect(asd, effect)
                }
            }
        }
    }
}