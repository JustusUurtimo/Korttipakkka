package com.sq.thed_ck_licker.ecs.systems.helperSystems

import android.util.Log
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import com.sq.thed_ck_licker.ecs.managers.ComponentManager
import com.sq.thed_ck_licker.ecs.managers.EntityId
import com.sq.thed_ck_licker.ecs.managers.add
import com.sq.thed_ck_licker.ecs.managers.difference
import com.sq.thed_ck_licker.ecs.managers.get

fun multiplyEntityValues(oldEntityId: EntityId, targetEntityId: EntityId) {
    if (oldEntityId == targetEntityId) return

    val difference = targetEntityId difference oldEntityId
    val diffComponents = ComponentManager.componentManager.getAllComponentsOfEntity(difference)

    val multiplier = (targetEntityId get MultiplierComponent::class).multiplier - 1

    for (component in diffComponents) {
//        println("Component1 of type ${component::class.simpleName} added")
//        println("Component1: $component")
        when (component) {
//            is MultiplierComponent -> {
//                component.multiplier = multiplier
//            }

            is HealthComponent -> {
                component.setHealth(component.getHealth() * multiplier)
                targetEntityId add (targetEntityId get HealthComponent::class).combineHealthComponents(component)
//                targetEntityId add component
            }

            is ScoreComponent -> {
                component.setScore((component.getScore() * multiplier).toInt())
               targetEntityId add (targetEntityId get ScoreComponent::class).combineScoreComponents(component)
            }

            else -> {
                Log.i(
                    "Multiplication system",
                    "Component of type ${component::class.simpleName} not supported"
                )
                continue
            }
        }
//        println("Component of type ${component::class.simpleName} added")
//        println("Component: $component")
//        println("score ${(targetEntityId get HealthComponent::class).getHealth()}")
//        targetEntityId add component


    }

}