package com.sq.thed_ck_licker.ecs.systems

import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.components.DescriptionComponent
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import com.sq.thed_ck_licker.ecs.components.addHealth
import com.sq.thed_ck_licker.ecs.components.addScore

class DescriptionSystem private constructor(private val componentManager: ComponentManager = ComponentManager.componentManager) {

    companion object {
        val instance: DescriptionSystem by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DescriptionSystem(ComponentManager.componentManager)
        }
    }

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
        entity: Int,
        componentManager: ComponentManager = this.componentManager
    ) {
        val comps = componentManager.getAllComponentsOfEntity(entity)
        val descComp = componentManager.getComponent(entity, DescriptionComponent::class)
        descComp.description.value = ""
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