package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import com.sq.thed_ck_licker.ecs.systems.helperSystems.DescriptionSystem
import javax.inject.Inject

class WorldCreationSystem @Inject constructor(
    private val componentManager: ComponentManager,
    private val playerSystem: PlayerSystem,
    private val merchantSystem: MerchantSystem,
    private val descriptionSystem: DescriptionSystem
) {

    fun destroyWorld() {
        componentManager.clear()
        initWorld()
    }

    fun createWorld() {
        descriptionSystem.updateAllDescriptions()
    }

    private fun initWorld() {
        playerSystem.initPlayer()
        merchantSystem.initRegularMerchant()
        descriptionSystem.updateAllDescriptions()
    }

}

