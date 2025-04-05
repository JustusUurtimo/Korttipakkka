package com.sq.thed_ck_licker.ecs

import com.sq.thed_ck_licker.ecs.components.CardTag
import com.sq.thed_ck_licker.ecs.components.TagsComponent
import kotlin.reflect.KClass

// Component Manager
class ComponentManager {

    companion object {
         val componentManager: ComponentManager = ComponentManager()
    }

    private val components = mutableMapOf<KClass<*>, MutableMap<Int, Any>>()

    fun <T : Any> addComponent(entity: Int, component: T) {
        components.getOrPut(component::class) { mutableMapOf() }[entity] = component
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

    fun getEntitiesWithTags(tags: List<CardTag>): Map<Int, Any>? {
        val entities = getEntitiesWithComponent(TagsComponent::class)
        val matchingEntities = entities?.filter { (_, value) ->
            (value as TagsComponent).tags.containsAll(tags)
        }
        return matchingEntities
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
}