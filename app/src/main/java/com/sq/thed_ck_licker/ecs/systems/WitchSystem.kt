package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.components.effectthing.damageEffects.TakeDamage
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import javax.inject.Inject

class WitchSystem @Inject constructor(
    private val playerSystem: PlayerSystem,
) {

    fun rejectOffering() {
        TakeDamage(20f)
    }
}