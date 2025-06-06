package com.sq.thed_ck_licker.ecs.systems.helperSystems

import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import javax.inject.Inject
import com.sq.thed_ck_licker.ecs.managers.get

class DescriptionSystem @Inject constructor(private val componentManager: ComponentManager) {

    fun updateAllDescriptions(componentManager: ComponentManager = this.componentManager) {
        val entitiesWithDescription =
            componentManager.getEntitiesWithComponent(DescriptionComponent::class)

        if (entitiesWithDescription == null) {
            println("No entities with description found")
            return
        }

        println("Entities with description: $entitiesWithDescription")

        for (entity in entitiesWithDescription) {
            val comps = componentManager.getAllComponentsOfEntity(entity.key)
            val descComp = entity.value as DescriptionComponent
            for (comp in comps) {
                when (comp) {
                    is ScoreComponent -> descComp.addScore(comp)
                    is HealthComponent -> descComp.addHealth(comp)
                    else -> {
                        println("Unknown component type: $comp")
                    }
                }
            }
        }
    }

    fun updateSingleDescription(
        entity: Int
    ) {
        val comps = componentManager.getAllComponentsOfEntity(entity)
        val descComp = (entity get DescriptionComponent::class)
        descComp.setDescription("")
        for (comp in comps) {
            when (comp) {
                is ScoreComponent -> descComp.addScore(comp)
                is HealthComponent -> descComp.addHealth(comp)
                else -> {
                    println("Unknown component type: $comp")
                }
            }
        }
    }
}