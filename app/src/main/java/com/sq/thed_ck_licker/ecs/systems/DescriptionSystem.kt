package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.ComponentManager
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent

class DescriptionSystem {

    fun updateDescriptions(componentManager: ComponentManager) {
        val entitiesWithDescription = componentManager.getEntitiesWithComponent(DescriptionComponent::class)


    }
}