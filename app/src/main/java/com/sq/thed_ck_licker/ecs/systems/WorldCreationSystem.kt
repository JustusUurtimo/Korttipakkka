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

    fun destroyWorldAndInitNewOne() {
        destroyWorld()
        initWorld()
    }

    private fun destroyWorld() {
        componentManager.clear()
        GameEvents.resetEventStream()
        MerchantEvents.resetEventStream()
    }

    private fun initWorld() {
        // Reset any stored flows or state in ViewModels
        playerSystem.initPlayer()
        merchantSystem.initRegularMerchant()
    }

}

