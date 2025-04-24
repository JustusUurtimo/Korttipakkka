package com.sq.thed_ck_licker.ecs

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.sq.thed_ck_licker.ecs.components.HealthComponent
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.ScoreComponent
import kotlin.reflect.KClass

class ComponentManager private constructor() {

    companion object {
        val componentManager: ComponentManager = ComponentManager()
    }

    private val components = mutableStateMapOf<KClass<*>, SnapshotStateMap<Int, Any>>()

    fun <T : Any> addComponent(entity: Int, component: T) {
        components.getOrPut(component::class) { mutableStateMapOf() }[entity] = component
    }

    fun <T : Any> getComponent(entity: Int, componentClass: KClass<T>): T {
        val componentMap = components[componentClass]
            ?: throw IllegalStateException("No components of type ${componentClass.simpleName} found")

        val component = componentMap[entity]
            ?: throw IllegalStateException("Component of type ${componentClass.simpleName} not found for entity $entity")

        // Check the type of the component before casting
        if (componentClass.isInstance(component)) {
            @Suppress("UNCHECKED_CAST") // Safe cast after type check
            return component as T
        } else {
            throw IllegalStateException("Component for entity $entity is not of type ${componentClass.simpleName}")
        }
    }


    fun <T : Any> getEntitiesWithComponent(componentClass: KClass<T>): Map<Int, Any>? {
        return components[componentClass]
    }


    fun getAllComponentsOfEntity(entityId: Int): List<Any> {
        val result = ArrayList<Any>()
        for (componentMap in components.values) {
            val component = componentMap[entityId]
            if (component != null) {
                result.add(component)
            }
        }
        return result
    }


    fun removeEntity(entity: Int) {
        for (componentMap in components.values) {
            componentMap.remove(entity)
        }
    }

    fun copy(entity: EntityId): EntityId {
        val entityCopy = EntityManager.createNewEntity()
        for (component in getAllComponentsOfEntity(entity)) {
            val copiedComponent = deepCopyComponent(component)
            if (copiedComponent == null) continue
            entityCopy add copiedComponent
        }

        return entityCopy
    }

    fun deepCopyComponent(component: Any): Any? {
        return when (component) {
            is HealthComponent -> HealthComponent(
                component.health,
                component.maxHealth
            )

            is ScoreComponent -> ScoreComponent(component.score)
            is MultiplierComponent -> MultiplierComponent(component.multiplier)
            else -> null
        }
    }
}

/**
 * Extension function to add a component to an entity.
 * Please do not try to use for any other add operation...
 */
infix fun <T : Any> EntityId.add(component: T) {
    ComponentManager.componentManager.addComponent(this, component)
}

infix fun <T : Any> EntityId.get(componentClass: KClass<T>): T {
    return ComponentManager.componentManager.getComponent(this, componentClass)
}

/**
 * For example:
 * Can be used to get if component is in the return value of getAllComponentsOfEntity()
 */
inline fun <reified T> List<Any>.hasComponent(): Boolean {
    return any { it is T }
}


infix fun EntityId.difference(entity: EntityId): EntityId {
    val result = EntityManager.createNewEntity()
    val entity1Components = ComponentManager.componentManager.getAllComponentsOfEntity(this)



    for (component in entity1Components) {
        var secondComponent: Any? = null
        try {
            secondComponent = entity get component::class
        } catch (_: Exception) {
            continue
        }
        result add when (component) {
            is HealthComponent -> {
                secondComponent as HealthComponent
                HealthComponent(
                    component.health - secondComponent.health,
                    component.maxHealth - secondComponent.maxHealth
                )
            }

            is ScoreComponent -> {
                secondComponent as ScoreComponent
                ScoreComponent(component.score - secondComponent.score)
            }

            is MultiplierComponent -> {
                secondComponent as MultiplierComponent
                MultiplierComponent(component.multiplier - secondComponent.multiplier)
            }

            else -> {
                Log.i(
                    "Entity Difference",
                    "One of the entities did not have this component: ${component::class.simpleName}"
                )
            }
        }
    }
    return result
}