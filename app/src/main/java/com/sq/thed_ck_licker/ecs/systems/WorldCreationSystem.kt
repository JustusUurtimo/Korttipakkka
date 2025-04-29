package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import jakarta.inject.Inject
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem.Companion.instance as descriptionSystem


class WorldCreationSystem @Inject constructor(
    private val componentManager: ComponentManager,
    private val playerSystem: PlayerSystem,
    private val merchantSystem: MerchantSystem
) {

    fun destroyWorld() {
        componentManager.clear()
        initWorld()
    }

    fun initWorld() {
        playerSystem.initPlayer()
        merchantSystem.initRegularMerchant()
        descriptionSystem.updateAllDescriptions()
    }

}

