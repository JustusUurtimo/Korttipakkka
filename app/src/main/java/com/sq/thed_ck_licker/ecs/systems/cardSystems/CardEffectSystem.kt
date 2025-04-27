package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get

class CardEffectSystem private constructor(@Suppress("unused") private val componentManager: ComponentManager) {

    companion object {
        val instance: CardEffectSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            CardEffectSystem(ComponentManager.componentManager)
        }
    }

    fun onHealthChanged(entity: Int) {
        val health = (entity get HealthComponent::class).health.floatValue

        if (health <= 0) {
            GameEvents.onPlayerDied.tryEmit(Unit)
        }
    }
}