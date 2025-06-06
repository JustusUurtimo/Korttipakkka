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
import javax.inject.Inject

class MultiplierSystem @Inject constructor(private val componentManager: ComponentManager) {
    val entityHolder = HashMap<EntityId, EntityId>()

    fun addEntity(entityId: EntityId) {
        entityHolder.put(entityId, componentManager.copy(entityId))
    }

    fun multiplyEntityAgainstOldItself(entityId: EntityId) =
        multiplyEntityValues(
            entityHolder.getOrDefault(entityId, componentManager.copy(entityId)),
            entityId
        )


    fun multiplyEntityValues(oldEntityId: EntityId, targetEntityId: EntityId) {
        if (oldEntityId == targetEntityId) return

        val difference = targetEntityId difference oldEntityId
        val diffComponents = componentManager.getAllComponentsOfEntity(difference)

        val multiplier = (targetEntityId get MultiplierComponent::class).multiplier - 1
        for (component in diffComponents) {
            var diffi: Number = 0
            when (component) {
                //            is MultiplierComponent -> {
                //                component.multiplier = multiplier
                //            }

                is HealthComponent -> {
                    component.setHealth(component.getHealth() * multiplier)
                    diffi = component.getHealth()
                    targetEntityId add (targetEntityId get HealthComponent::class).combineHealthComponents(
                        component
                    )
                }

                is ScoreComponent -> {
                    component.setScore((component.getScore() * multiplier).toInt())
                    diffi = component.getScore()
                    targetEntityId add (targetEntityId get ScoreComponent::class).combineScoreComponents(
                        component
                    )
                }

                else -> {
                    Log.i(
                        "Multiplication system",
                        "Component of type ${component::class.simpleName} not supported"
                    )
                    continue
                }
            }
            Log.v(
                "Multiplication system", "Multiplier gave you just $diffi of something"
            )
        }
    }
}