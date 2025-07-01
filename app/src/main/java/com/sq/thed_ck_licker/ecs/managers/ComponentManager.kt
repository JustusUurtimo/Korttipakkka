package com.sq.thed_ck_licker.ecs.managers

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.sq.thed_ck_licker.ecs.components.MultiplierComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import com.sq.thed_ck_licker.ecs.components.TagsComponent.Tag
import com.sq.thed_ck_licker.ecs.components.misc.HealthComponent
import com.sq.thed_ck_licker.ecs.components.misc.ScoreComponent
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class ComponentManager {

    companion object {
        val componentManager: ComponentManager = ComponentManager()
    }

    private val components = mutableStateMapOf<KClass<*>, SnapshotStateMap<Int, Any>>()

    fun <T : Any> addComponent(entity: Int, component: T) {
        components.getOrPut(component::class) { mutableStateMapOf() }[entity] = component
    }

    fun <T : Any> getComponent(entity: Int, componentClass: KClass<T>): T {
        val componentMap = components[componentClass]
            ?: throw IllegalStateException("No components of type ${componentClass.simpleName} found for entity $entity")

        val component = componentMap[entity]
            ?: throw IllegalStateException("Component of type ${componentClass.simpleName} not found for entity $entity")

        // Check the type of the component before casting
        if (componentClass.isInstance(component)) {
            return component as T
        } else {
            throw IllegalStateException("Component for entity $entity is not of type ${componentClass.simpleName}")
        }
    }


    fun <T : Any> getEntitiesWithComponent(componentClass: KClass<T>): Map<Int, T>? {
        return components[componentClass] as? Map<Int, T>
    }


    fun <T : Any> getEntitiesWithTheseComponents(componentClasses: List<KClass<T>>): List<EntityId> {
        val result = mutableListOf<List<EntityId>>()
        for (componentClass in componentClasses) {
            val componentMap = components[componentClass]
            if (componentMap != null) {
                result.add(componentMap.keys.toList())
            }
        }
        val result2 = result.flatten().groupingBy { it }.eachCount()
            .filter { it.value >= result.size }.keys.toList()
        return result2
    }


    fun getEntitiesWithTags(tags: List<Tag>): Map<Int, Any> {
        val entities = getEntitiesWithComponent(TagsComponent::class)
        checkNotNull(entities) { "No entities with TagsComponent found" }
        val matchingEntities = entities.filter { (_, value) ->
            (value).getTags().containsAll(tags)
        }
        return matchingEntities
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

    fun removeComponent(entity: Int, componentClass: KClass<*>) {
        val componentMap = components[componentClass]
        componentMap?.remove(entity)
    }

    fun removeEntity(entity: Int) {
        for (componentMap in components.values) {
            componentMap.remove(entity)
        }
    }

    fun clear() {
        components.clear()
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
                component.getHealth(),
                component.getMaxHealth()
            )
            is ScoreComponent -> ScoreComponent(component.getScore())
            is MultiplierComponent -> MultiplierComponent(component.multiplier)
            else -> {
                Log.w("ComponentManager", "Unknown component type: ${component.javaClass.name}, it was not copied.")
                return null
            }
        }
    }
}

/**
 * Extension function to add a component to an entity.
 * Please do not try to use for any other add operation...
 */
infix fun <T : Any> EntityId.add(component: T): T {
    ComponentManager.componentManager.addComponent(this, component)
    return component
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
        var secondComponent: Any
        try {
            secondComponent = entity get component::class
        } catch (_: Exception) {
            continue
        }
        result add when (component) {
            is HealthComponent -> {
                secondComponent as HealthComponent
                if (component.getHealth() - secondComponent.getHealth() == 0f &&
                    component.getMaxHealth() - secondComponent.getMaxHealth() == 0f
                ) continue
                HealthComponent(
                    component.getHealth() - secondComponent.getHealth(),
                    component.getMaxHealth() - secondComponent.getMaxHealth(),
                )
            }

            is ScoreComponent -> {
                secondComponent as ScoreComponent
                if (component.getScore() - secondComponent.getScore() == 0) continue
                ScoreComponent(component.getScore() - secondComponent.getScore())
            }

            is MultiplierComponent -> {
                secondComponent as MultiplierComponent
                if (component.multiplier - secondComponent.multiplier == 0f) continue
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