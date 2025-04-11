package com.sq.thed_ck_licker.ecs

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import kotlinx.parcelize.IgnoredOnParcel
import kotlin.reflect.KClass

// Component Manager
class ComponentManager private constructor() {

    companion object {
        val componentManager: ComponentManager = ComponentManager()
    }

    @IgnoredOnParcel
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


    fun getEntitiesWithTags(tags: List<CardTag>): Map<Int, Any> {
        val entities = getEntitiesWithComponent(TagsComponent::class)
        if (entities == null) {
            throw IllegalStateException("No entities with TagsComponent found")
        } else {
            val matchingEntities = entities.filter { (_, value) ->
                (value as TagsComponent).tags.containsAll(tags)
            }
            return matchingEntities
        }
    }

    /* TODO: There is argument for having two lists,
     *  one from the component view and one from the entity view
     *  When made correctly and as private things that the Component manager controls,
     *  it will be really easy to uphold both of them.
     *  Thou it might also be premature optimization.
     */
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