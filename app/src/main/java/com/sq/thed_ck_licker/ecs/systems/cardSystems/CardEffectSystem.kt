package com.sq.thed_ck_licker.ecs.systems.cardSystems

import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.get
import jakarta.inject.Inject

class CardEffectSystem @Inject constructor() {

    fun onHealthChanged(entity: Int) {
        val health = (entity get HealthComponent::class).health.floatValue

        if (health <= 0) {
            GameEvents.onPlayerDied.tryEmit(Unit)
        }
    }
}