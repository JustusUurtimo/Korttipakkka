package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.GameEvents
import com.sq.thed_ck_licker.ecs.managers.MerchantEvents
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import javax.inject.Inject

class WorldCreationSystem @Inject constructor(
    private val componentManager: ComponentManager,
    private val playerSystem: PlayerSystem,
    private val merchantSystem: MerchantSystem,
) {

    fun destroyWorld() {
        componentManager.clear()
        GameEvents.resetEventStream()
        MerchantEvents.resetEventStream()
        // Reset any stored flows or state in ViewModels
        initWorld()
    }

    private fun initWorld() {
        playerSystem.initPlayer()
        merchantSystem.initRegularMerchant()
    }

}

