package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.systems.DescriptionSystem.Companion.instance as descriptionSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.MerchantSystem.Companion.instance as merchantSystem
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem.Companion.instance as playerSystem

class WorldCreationSystem private constructor(private val componentManager: ComponentManager = ComponentManager.componentManager) {

    companion object {
        val WorldCreationSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            WorldCreationSystem(ComponentManager.componentManager)
        }
    }

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

